package com.example.sakunusa.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sakunusa.data.Result
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.databinding.FragmentStatisticsBinding
import com.example.sakunusa.factory.ViewModelFactory
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartSymbolStyleType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aachartcreator.aa_toAAOptions
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
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
                is Result.Success -> setupChart(binding.chart, result.data)
                is Result.Error -> {}
                // showError(result.exception.message)
                is Result.Loading -> {}
                // showLoadingIndicator()
            }
        }
    }

    private fun setupChart(
        aaChartView: AAChartView,
        records: List<RecordEntity>,
    ) {
        val simplifiedRecords = simplifyAmountPerDay(records)
        val days: Array<String> = simplifiedRecords.keys.toTypedArray()
        val expenses: Array<Any> = simplifiedRecords.values.toTypedArray()

        val series: Array<AASeriesElement> = arrayOf(
            AASeriesElement()
                .name("Expense")
                .data(expenses)
                .borderRadiusBottomLeft(20)
                .borderRadiusBottomRight(20)
                .borderRadiusTopLeft(20)
                .borderRadiusTopRight(20)
        )

        // draw chart


        if (!isChartCreated) {
            val genericSeries: Array<Any> = Array(series.size) { series[it] }

            val aaTooltip = AATooltip()
                .useHTML(true)
                .formatter(
                    "function () {return this.x + ': Rp. ' + this.y;}"
                )
                .valueDecimals(2)
                .backgroundColor("#000000")
                .borderColor("#000000")
                .style(
                    AAStyle()
                        .color("#ffffff")
                        .fontSize(12)
                )

            val aaChartModel: AAChartModel = AAChartModel()
                .chartType(AAChartType.Line)
                .title("title")
                .categories(days)

                .backgroundColor(com.google.android.material.R.color.mtrl_btn_transparent_bg_color)
                .dataLabelsEnabled(false)
                .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank)

                .markerRadius(0)
                .yAxisTitle("")
                .yAxisLabelsEnabled(true)
                .series(genericSeries)
                .colorsTheme(
                    arrayOf(
                        "#fe117c",
                    )
                )

            val aaOptions = aaChartModel.aa_toAAOptions()
                .tooltip(aaTooltip)

            aaChartView.aa_drawChartWithChartModel(aaChartModel)
            aaChartView.aa_drawChartWithChartOptions(aaOptions)

            isChartCreated = true
        } else {
            aaChartView.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(
                series, true
            )
        }
    }

    private fun simplifyAmountPerDay(data: List<RecordEntity>): Map<String, Float> {
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

        return data.sortedBy { it.dateTime }
            .groupBy { record ->
                dateFormat.format(java.util.Date(record.dateTime)) // Convert datetime to a "yyyy-MM-dd" string
            }.mapValues { entry ->
                return@mapValues entry.value.sumOf { it.amount.toDouble() }
                    .toFloat()
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}