package com.example.sakunusa.ui.newrecord

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sakunusa.R
import com.example.sakunusa.data.Result
import com.example.sakunusa.data.local.entity.AccountEntity
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.databinding.ActivityNewRecordBinding
import com.example.sakunusa.factory.ViewModelFactory
import com.example.sakunusa.utils.SpinnerItem
import com.example.sakunusa.utils.Utils
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import android.R as AndroidR

class NewRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewRecordBinding

    private var isEdit: Boolean = false
    private var recordId: Int = -1

    private val categories = listOf(
        "None",
        "Food",
        "Transport",
        "Entertainment",
        "Bills",
        "Health",
        "Gift",
        "Investment",
        "Salary",
        "Freelance",
        "Others",
    )

    private val viewModel: NewRecordViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        recordId = intent.getIntExtra(EXTRA_RECORD_ID, -1)

        if (recordId != -1) {
            isEdit = true

            viewModel.fetchRecordById(recordId)

            viewModel.record.observe(this) { record ->
                if (record != null) {
                    populateFormFields(record)
                } else {
                    showToast("Error when try to edit record")
                }
            }
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = if (isEdit) "Edit Record" else "New Record"

        binding.btnPickDateTime.setOnClickListener {
            pickDateTime()
        }
        val spinnerCategoryAdapter =
            ArrayAdapter(this, R.layout.item_spinner, categories).apply {
                setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
            }
        binding.spinnerCategory.adapter = spinnerCategoryAdapter


        viewModel.getAccounts().observe(this) { result: Result<List<AccountEntity>> ->
            when (result) {
                is Result.Success -> {
                    val spinnerItems: List<SpinnerItem> =
                        result.data.map { SpinnerItem(it.id, it.name) }

                    val spinnerAccountAdapter =
                        ArrayAdapter(
                            this,
                            AndroidR.layout.simple_spinner_item,
                            spinnerItems
                        ).apply {
                            setDropDownViewResource(AndroidR.layout.simple_spinner_dropdown_item)
                        }

                    binding.spinnerAccount.adapter = spinnerAccountAdapter
                }

                is Result.Error -> {
                }

                Result.Loading -> {
                }
            }
        }

        viewModel.selectedDate.observe(this) {
            binding.btnPickDateTime.text = Utils.formatDate(it)
        }

        binding.btnSave.setOnClickListener {
            with(binding) {
                var amount = etAmount.text.toString().trim().toFloat()
                val category = categories[spinnerCategory.selectedItemPosition]
                val selectedItem = spinnerAccount.selectedItem as SpinnerItem
                val account = selectedItem.id
                val description = etDescription.text.toString().trim()
                val type = when (binding.rgType.checkedRadioButtonId) {
                    binding.rbIncome.id -> 1
                    binding.rbExpense.id -> 0
                    else -> -1
                }

                if (type == 0 && amount > 0) {
                    amount *= -1
                }

                if (isEdit) {
                    val record = RecordEntity(
                        id = recordId,
                        amount = amount,
                        type = type,
                        accountId = account,
                        category = category,
                        dateTime = viewModel.selectedDate.value ?: 0,
                        description = description,
                    )
                    viewModel.updateRecord(record)
                } else {
                    val record = RecordEntity(
                        id = 0,
                        amount = amount,
                        type = type,
                        accountId = account,
                        category = category,
                        dateTime = viewModel.selectedDate.value ?: 0,
                        description = description,
                    )
                    viewModel.addRecord(record)
                }

                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun pickDateTime() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->
                    val selectedDateTime = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth, hour, minute)
                    }.timeInMillis

                    viewModel.setSelectedDate(selectedDateTime)

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun validateEmptyInputs(views: Array<TextInputEditText>): Boolean {
        val validationResults = views.map {
            if (it.text.toString().trim().isEmpty()) {
                it.error = "Required"
                false
            } else true
        }

        return validationResults.all { it }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.record_form, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                deleteRecord(recordId)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteRecord(recordId: Int) {
        viewModel.deleteRecord(recordId) { success ->
            if (success) {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun populateFormFields(record: RecordEntity) {
        val amount = record.amount * if (record.type == 0) -1 else 1

        viewModel.setSelectedDate(record.dateTime)
        binding.etDescription.setText(record.description)
        binding.etAmount.setText(amount.toInt().toString())
        binding.spinnerCategory.setSelection(categories.indexOf(record.category))
        binding.rgType.check(
            when (record.type) {
                1 -> binding.rbIncome.id
                0 -> binding.rbExpense.id
                else -> 0
            }
        )
    }

    companion object {
        const val EXTRA_RECORD_ID = "extra_record_id"
    }
}