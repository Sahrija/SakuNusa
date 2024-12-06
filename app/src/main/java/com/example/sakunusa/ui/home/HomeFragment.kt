package com.example.sakunusa.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sakunusa.data.Result
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.databinding.FragmentHomeBinding
import com.example.sakunusa.factory.ViewModelFactory
import com.example.sakunusa.ui.adapter.RecordAdapter
import com.example.sakunusa.ui.newrecord.NewRecordActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = ViewModelFactory.getInstance(requireActivity())

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
    }

    private fun setUpAdapter() {
        val adapter = RecordAdapter(onclick = {
            editRecord(it.id)
        })

        homeViewModel.records.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        adapter.submitList(result.data)
                    }

                    is Result.Error -> {
                    }

                    Result.Loading -> {
                    }
                }
            }
        }

        binding.rvRecords.apply {
            this.adapter = adapter
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
        Log.d("Test id", recordId.toString())
        val intent = Intent(requireActivity(), NewRecordActivity::class.java)
        intent.putExtra(NewRecordActivity.EXTRA_RECORD_ID, recordId)
        newRecordLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}