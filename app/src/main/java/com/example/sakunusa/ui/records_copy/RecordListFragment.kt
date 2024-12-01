package com.example.sakunusa.ui.records_copy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.sakunusa.data.Result
import com.example.sakunusa.databinding.FragmentRecordListBinding
import com.example.sakunusa.databinding.ViewTwoBinding
import com.example.sakunusa.factory.ViewModelFactory
import com.example.sakunusa.ui.adapter.RecordAdapter
import com.example.sakunusa.ui.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RecordListFragment : Fragment() {

    private var _binding: FragmentRecordListBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: RecordListViewModel

    lateinit var fragmentTwoBinding : ViewTwoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecordListBinding.inflate(inflater, container, false)
        fragmentTwoBinding = ViewTwoBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[RecordListViewModel::class.java]

//        setUpAdapter()

        setupViewPager2()

    }

    private fun setupViewPager2() {

        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        // Set up the ViewPager2 adapter
        val adapter = ViewPagerAdapter()
        viewPager.adapter = adapter

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.tabTitles[position] // Set tab titles
        }.attach()
    }

    private fun setUpAdapter() {
        val adapter = RecordAdapter()

        viewModel.records.observe(viewLifecycleOwner) { result ->
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