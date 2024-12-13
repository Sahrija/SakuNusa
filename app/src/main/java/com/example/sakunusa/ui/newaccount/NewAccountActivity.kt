package com.example.sakunusa.ui.newaccount

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sakunusa.R
import com.example.sakunusa.data.local.entity.AccountEntity
import com.example.sakunusa.databinding.ActivityNewAccountBinding
import com.example.sakunusa.databinding.ActivityNewRecordBinding
import com.example.sakunusa.factory.ViewModelFactory

class NewAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewAccountBinding

    private val viewModel: NewAccountViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)

        val accountId = intent.getIntExtra(EXTRA_ACCOUNT_ID, 0)
        if (accountId != 0) {
            isEdit = true
            supportActionBar?.title = "Edit Account"

            viewModel.getAccountById(accountId).observe(this) { account ->
                binding.etName.setText(account.name)
                binding.etStartingAmount.setText(account.startingAmount.toString())
            }
        }

        binding.btnSave.setOnClickListener {

            val name = binding.etName.text.toString()
            val startingAmount: Float =
                binding.etStartingAmount.text.toString().toFloatOrNull() ?: 0f

            val account = AccountEntity(accountId, name, startingAmount, false)

            if (isEdit) {
                updateAccount(account)
            } else {
                newAccount(account)
            }
        }
    }

    private fun updateAccount(account: AccountEntity) {
        viewModel.updateAccount(account)
        finish()
    }

    private fun newAccount(account: AccountEntity) {
        viewModel.newAccount(
            account,
            onResult = { isSuccess ->
                if (isSuccess) {
                    finish()
                }
            }
        )
    }

    companion object {
        const val EXTRA_ACCOUNT_ID = "extra_account_id"
    }
}