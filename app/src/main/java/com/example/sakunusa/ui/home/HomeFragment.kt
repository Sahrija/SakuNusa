package com.example.sakunusa.ui.home

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sakunusa.data.Result
import com.example.sakunusa.data.local.entity.AccountEntity
import com.example.sakunusa.databinding.FragmentHomeBinding
import com.example.sakunusa.factory.ViewModelFactory
import com.example.sakunusa.ui.adapter.AccountAdapter
import com.example.sakunusa.ui.adapter.RecordAdapter
import com.example.sakunusa.ui.newaccount.NewAccountActivity
import com.example.sakunusa.ui.newrecord.NewRecordActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var accountAdapter: AccountAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNewAccount.setOnClickListener {
            val intent = Intent(requireActivity(), NewAccountActivity::class.java)
            newAccountLauncher.launch(intent)
        }

        setUpAdapter()
    }

    private fun setUpAdapter() {
        val recordAdapter = RecordAdapter(onclick = {
            editRecord(it.id)
        })

        homeViewModel.records.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> recordAdapter.submitList(result.data)
                    is Result.Error -> {}
                    is Result.Loading -> {}
                }
            }
        }

        binding.rvRecords.apply {
            this.adapter = recordAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        // =============

        accountAdapter = AccountAdapter(
            onClick = {
                homeViewModel.toggleAccountToSelected(it)
            }, onLongClick = {
                editAccount(it.id)
            })

        homeViewModel.getAccounts()
            .observe(viewLifecycleOwner) { result: Result<List<AccountEntity>> ->
                when (result) {
                    is Result.Success -> {
                        accountAdapter.submitList(result.data)
                        homeViewModel.fetchRecords()
                    }
                    is Result.Error -> {}
                    Result.Loading -> {}
                }
            }


        val itemSpacing = 16
        binding.rvAccounts.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
            ) {
                outRect.right = itemSpacing
            }
        })


        binding.rvAccounts.apply {
            this.adapter = accountAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

    }

    private fun editAccount(id: Int) {
        val intent = Intent(requireActivity(), NewAccountActivity::class.java)
        intent.putExtra(NewAccountActivity.EXTRA_ACCOUNT_ID, id)
        newAccountLauncher.launch(intent)
    }

    private val newRecordLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
            }
        }

    private val newAccountLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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