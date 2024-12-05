package com.example.sakunusa.ui.records

import android.app.Activity
import android.content.Intent
import com.example.sakunusa.ui.adapter.GroupedRecordAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sakunusa.data.Result
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.databinding.FragmentRecordsBinding
import com.example.sakunusa.factory.ViewModelFactory
import com.example.sakunusa.model.RecordItem
import com.example.sakunusa.ui.newrecord.NewRecordActivity

class RecordsFragment : Fragment() {

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recordsViewModel: RecordsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        recordsViewModel = ViewModelProvider(this, factory)[RecordsViewModel::class.java]

        setUpAdapter()
    }

    private fun setUpAdapter() {
        var adapter: GroupedRecordAdapter?

        recordsViewModel.records.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        val records: List<RecordEntity> = result.data
                        val groupedList = RecordItem.groupRecords(records, RecordItem.GROUP_BY_DAY)
                        adapter = GroupedRecordAdapter(groupedList,
                            onclick = { recordId: Int ->
                                editRecord(recordId)
                            })

                        binding.rvRecords.apply {
                            this.adapter = adapter
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                        }
//                        adapter.submitList(groupedList)
                    }

                    is Result.Error -> {
                    }

                    Result.Loading -> {
                    }
                }
            }
        }
    }

    private val newRecordLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val newRecord = result.data?.getParcelableExtra<RecordEntity>("new_record")
                Toast.makeText(requireActivity(), "Edited", Toast.LENGTH_SHORT).show()
            }
        }

    private fun editRecord(recordId: Int) {
        val intent = Intent(requireActivity(), NewRecordActivity::class.java)
        intent.putExtra(NewRecordActivity.EXTRA_RECORD_ID, recordId)
        newRecordLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}