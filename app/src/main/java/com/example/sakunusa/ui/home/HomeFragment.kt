package com.example.sakunusa.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sakunusa.data.Result
import com.example.sakunusa.databinding.FragmentHomeBinding
import com.example.sakunusa.factory.ViewModelFactory
import com.example.sakunusa.ui.adapter.RecordAdapter
import com.example.sakunusa.ui.records_copy.RecordsActivity

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

        setUpAdapter()

        binding.btnAllRecords.setOnClickListener {
            val intent = Intent(requireActivity(), RecordsActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun setUpAdapter() {
        val adapter = RecordAdapter()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}