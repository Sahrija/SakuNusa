package com.example.sakunusa.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sakunusa.R
import com.example.sakunusa.data.Result
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.databinding.FragmentStatisticsBinding
import com.example.sakunusa.factory.ViewModelFactory
import com.example.sakunusa.utils.Utils
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aachartcreator.aa_toAAOptions
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AATooltip

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null

    private val binding get() = _binding!!

    private val statisticsViewModel: StatisticsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var isChartCreated: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statisticsViewModel.records.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> setupChart(result.data)
                is Result.Error -> {}
                // showError(result.exception.message)
                is Result.Loading -> {}
                // showLoadingIndicator()
            }
        }
    }


    private fun setupChart(
        records: List<RecordEntity>,
    ) {
        val lineChartView = binding.lineChartView

        val incomesRecords = records.filter { it.type == 1 }
        val expensesRecords = records.filter { it.type == 0 }
        val simplifiedIncomes = getLastSevenDays(incomesRecords)
        val simplifiedExpenses = getLastSevenDays(expensesRecords)
        val incomes: Array<Any> = simplifiedIncomes.values.toTypedArray()
        val expenseInt: List<Float> = simplifiedExpenses.values.map { it * -1 }
        val expenses: Array<Any> = Array(expenseInt.size) { expenseInt[it] }

        val days: Array<String> = simplifiedIncomes.keys.toTypedArray()

        val series: Array<AASeriesElement> = arrayOf(
            AASeriesElement()
                .name("Income")
                .data(incomes),
            AASeriesElement()
                .name("Expense")
                .data(expenses)
        )

        // draw chart
        if (!isChartCreated) {
            isChartCreated = true

            val genericSeries: Array<Any> = Array(series.size) { series[it] }

            val aaTooltip = AATooltip()
                .useHTML(true)
                .formatter(
                    """
function () {
        let wholeContentStr ='<span style=\"' + 'color:white; font-size:13px\"' + '>' + this.x + '</span><br/>';
        let length = this.points.length;
        for (let i = 0; i < length; i++) {
            let thisPoint = this.points[i];
            let prefix = (thisPoint.series.name === "Expense") ? 'Rp. -' : 'Rp.  ';
            let yValue = thisPoint.y;
            let spanStyleStartStr = '<span style=\"' + 'color:'+ thisPoint.color + '; font-size:13px\"' + '>● ';
            let spanStyleEndStr = '</span> <br/>';
            wholeContentStr += spanStyleStartStr + thisPoint.series.name + ': ' + prefix + thisPoint.y + spanStyleEndStr;
        }
        return wholeContentStr;
    }
"""
                )
                .backgroundColor("#050505")
                .borderColor("#050505")


            val aaChartModel: AAChartModel = AAChartModel()
                .chartType(AAChartType.Line)
                .title("Last 7 days")
                .categories(days)

                .backgroundColor(com.google.android.material.R.color.mtrl_btn_transparent_bg_color)
                .dataLabelsEnabled(false)

                .markerRadius(0)
                .yAxisTitle("")
                .yAxisLabelsEnabled(true)
                .series(genericSeries)
                .colorsTheme(
                    arrayOf(
                        Utils.getColorAsString(requireActivity(), R.color.green_500),
                        Utils.getColorAsString(requireActivity(), R.color.red_500),
                    )
                )

            val aaOptions = aaChartModel.aa_toAAOptions()
                .tooltip(aaTooltip)

            lineChartView.aa_drawChartWithChartModel(aaChartModel)
            lineChartView.aa_drawChartWithChartOptions(aaOptions)
        } else {
            lineChartView.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(
                series, true
            )
        }

        val pieChartView = binding.pieChartView
        val recordsByCategory: Map<String, Float> =
            records.filter { it.type == 0 }.groupBy { record ->
                record.category
            }.mapValues { entry ->
                entry.value.sumOf { it.amount.toDouble() * -1 }.toFloat()
            }

        val series2: Array<Any> = arrayOf(
            AASeriesElement()
                .data(
                    recordsByCategory.map {
                        arrayOf(it.key, it.value, "30")
                    }.toTypedArray()
                )
                .showInLegend(true)

        )

        val categories = recordsByCategory.keys.toTypedArray()


        val pieChartModel = AAChartModel()
            .chartType(AAChartType.Pie)
            .title("Expense Categories")
            .series(series2)
            .backgroundColor(com.google.android.material.R.color.mtrl_btn_transparent_bg_color)


        pieChartView.aa_drawChartWithChartModel(pieChartModel)
    }

    private fun simplifyAmountPerDay(data: List<RecordEntity>): Map<String, Float> {
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

        return data.sortedBy { it.dateTime }
            .groupBy { record ->
                dateFormat.format(java.util.Date(record.dateTime))
            }.mapValues { entry ->
                return@mapValues entry.value.sumOf { it.amount.toDouble() }
                    .toFloat()
            }
    }

    private fun getLastSevenDays(data: List<RecordEntity>): Map<String, Float> {
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

        val calendar = java.util.Calendar.getInstance()
        val last7Days = (0 until 7).map { offset ->
            calendar.time = java.util.Date()
            calendar.add(java.util.Calendar.DAY_OF_YEAR, -offset)
            dateFormat.format(calendar.time)
        }.reversed()

        val groupedData = data.groupBy { record ->
            dateFormat.format(java.util.Date(record.dateTime))
        }.mapValues { entry ->
            entry.value.sumOf { it.amount.toDouble() }.toFloat()
        }

        return last7Days.associateWith { date ->
            groupedData[date] ?: 0.0f
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}