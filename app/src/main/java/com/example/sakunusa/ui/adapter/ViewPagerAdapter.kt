package com.example.sakunusa.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.sakunusa.R
import com.example.sakunusa.databinding.ViewOneBinding
import com.example.sakunusa.databinding.ViewTwoBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
    private val layouts = listOf(
        R.layout.view_one,
        R.layout.view_two
    )

    companion object {
        const val VIEW_ONE = 0
        const val VIEW_TWO = 1
    }

    val tabTitles = listOf("Record List", "Analysis")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_ONE -> {
                val binding = ViewOneBinding.inflate(inflater, parent, false)
                ViewHolder(binding)
            }
            VIEW_TWO -> {
                val binding = ViewTwoBinding.inflate(inflater, parent, false)
                ViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> {
                val binding = holder.binding as ViewOneBinding

            }

            1 -> {
                val binding = holder.binding as ViewTwoBinding
                setupChart(binding)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_ONE
            1 -> VIEW_TWO
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getItemCount(): Int = 2

    class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    private fun setupChart(binding: ViewTwoBinding){
        val aaChartView : AAChartView = binding.aaChartView

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("title")
            .subtitle("subtitle")
            .backgroundColor(com.google.android.material.R.color.mtrl_btn_transparent_bg_color)
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("Tokyo")
                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
                AASeriesElement()
                    .name("NewYork")
                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)),
                AASeriesElement()
                    .name("London")
                    .data(arrayOf(0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0)),
                AASeriesElement()
                    .name("Berlin")
                    .data(arrayOf(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8))
            ))
            .colorsTheme(arrayOf(
                "#fe117c",
                "#ffc069",
                "#06caf4",
                "#7dffc0"))

        //The chart view object calls the instance object of AAChartModel and draws the final graphic
        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }

//
//    private fun setupChart(binding: ViewTwoBinding) {
//        val anyChartView: AnyChartView = binding.anyChartView
////        anyChartView.setProgressBar(binding.progressBar)
//
//        val cartesian = AnyChart.column()
//
//        val data: MutableList<DataEntry> = ArrayList()
//        data.add(ValueDataEntry("Rouge", 80540))
//        data.add(ValueDataEntry("Foundation", 94190))
//        data.add(ValueDataEntry("Mascara", 102610))
//        data.add(ValueDataEntry("Lip gloss", 110430))
//        data.add(ValueDataEntry("Lipstick", 128000))
//        data.add(ValueDataEntry("Nail polish", 143760))
//        data.add(ValueDataEntry("Eyebrow pencil", 170670))
//        data.add(ValueDataEntry("Eyeliner", 213210))
//        data.add(ValueDataEntry("Eyeshadows", 249980))
//
//        val column = cartesian.column(data)
//
//        column.tooltip()
//            .titleFormat("{%X}")
//            .position(Position.CENTER_BOTTOM)
//            .anchor(Anchor.CENTER_BOTTOM)
//            .offsetX(0.0)
//            .offsetY(5.0)
//            .format("\${%Value}{groupsSeparator: }")
//
//        cartesian.animation(true)
//        cartesian.title("Top 10 Cosmetic Products by Revenue")
//
//        cartesian.yScale().minimum(0.0)
//
//        cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")
//
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
//        cartesian.interactivity().hoverMode(HoverMode.BY_X)
//
//        cartesian.xAxis(0).title("Product")
//        cartesian.yAxis(0).title("Revenue")
//        cartesian.yAxis(0).drawFirstLabel(false)
//        cartesian.yAxis(0).drawFirstLabel(false)
//
//        anyChartView.setChart(cartesian)
//    }
//
}
