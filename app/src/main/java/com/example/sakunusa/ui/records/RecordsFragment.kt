package com.example.sakunusa.ui.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sakunusa.data.Result
import com.example.sakunusa.databinding.FragmentRecordsBinding
import com.example.sakunusa.factory.ViewModelFactory
import com.example.sakunusa.ui.adapter.RecordAdapter

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
        val adapter = RecordAdapter()

        recordsViewModel.records.observe(viewLifecycleOwner) { result ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}