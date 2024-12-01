package com.example.sakunusa.ui.records_copy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sakunusa.R
import com.example.sakunusa.databinding.FragmentSecondBinding
//import lecho.lib.hellocharts.model.Line
//import lecho.lib.hellocharts.model.LineChartData
//import lecho.lib.hellocharts.model.PointValue
//import lecho.lib.hellocharts.view.LineChartView


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root


//        setupChart()
    }

//    private fun setupChart() {
//        val values: MutableList<PointValue> = ArrayList()
//        values.add(PointValue(0f, 2f))
//        values.add(PointValue(1f, 4f))
//        values.add(PointValue(2f, 3f))
//        values.add(PointValue(3f, 4f))
//
//        //In most cased you can call data model methods in builder-pattern-like manner.
//
//        //In most cased you can call data model methods in builder-pattern-like manner.
//        val line = Line(values).setColor(Color.BLUE).setCubic(true)
//        val lines: MutableList<Line> = ArrayList()
//        lines.add(line)
//
//        val data = LineChartData()
//        data.setLines(lines)
//
//        val chart = binding.chart
//        chart.lineChartData = data
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}