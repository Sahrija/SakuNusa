package com.example.sakunusa.ui.analytics

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sakunusa.data.Result
import com.example.sakunusa.data.local.entity.AnomalyEntity
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.databinding.FragmentAnalyticsBinding
import com.example.sakunusa.factory.ViewModelFactory
import com.example.sakunusa.ui.adapter.AnomalyAdapter
import com.example.sakunusa.ui.newrecord.NewRecordActivity

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private val analyticsViewModel: AnalyticsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
    }

    private fun setUpAdapter() {
        val recordAdapter = AnomalyAdapter(
            onClick = { anomalyWithRecord ->
                anomalyWithRecord.record?.id?.let { recordId -> editRecord(recordId) }
            },
            onLongClick = { anomalyWithRecord ->
                showAnomalyOptionsDialog(anomalyWithRecord.anomaly)
            }
        )

        analyticsViewModel.anomalies().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> recordAdapter.submitList(result.data)
                    is Result.Error -> {}
                    is Result.Loading -> {}
                }
            }
        }

        binding.rvAnomalies.apply {
            this.adapter = recordAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private val newRecordLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<RecordEntity>("new_record")
            }
        }

    private fun editRecord(recordId: Int) {
        val intent = Intent(requireActivity(), NewRecordActivity::class.java)
        intent.putExtra(NewRecordActivity.EXTRA_RECORD_ID, recordId)
        newRecordLauncher.launch(intent)
    }

    private fun showAnomalyOptionsDialog(anomaly: AnomalyEntity) {

        val options = arrayOf(
            if (anomaly.anomalyDetected) "Ignore anomaly" else "Set as anomaly",
            "Delete",
        )

        AlertDialog.Builder(context)
            .setTitle("Choose an action")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> toggleAnomaly(anomaly)
                    1 -> deleteAnomaly(anomaly)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun deleteAnomaly(anomaly: AnomalyEntity) {
        analyticsViewModel.deleteAnomaly(anomaly)
    }

    private fun toggleAnomaly(anomaly: AnomalyEntity) {
        analyticsViewModel.toggleAnomaly(anomaly)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}