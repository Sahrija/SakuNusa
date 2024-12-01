package com.example.sakunusa.ui.newrecord

import android.R
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.databinding.ActivityNewRecordBinding
import com.example.sakunusa.factory.ViewModelFactory
import com.example.sakunusa.utils.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class NewRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewRecordBinding

    private lateinit var viewModel: NewRecordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPickDateTime.setOnClickListener {
            pickDateTime()
        }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[NewRecordViewModel::class.java]

        val spinnerCategory: Spinner = binding.spinnerCategory
        val categories = listOf("None", "Food", "Transport", "Entertainment", "Bills", "Others")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }
        spinnerCategory.adapter = adapter

        viewModel.selectedDate.observe(this) {
            binding.btnPickDateTime.text = Utils.formatDate(it)
        }

        binding.btnSubmit.setOnClickListener {
            with(binding) {
                val amount = etAmount.text.toString().trim().toFloat()
                val category = categories[spinnerCategory.selectedItemPosition]
                val description = etDescription.text.toString().trim()


                val record = RecordEntity(
                    id = 0,
                    amount = amount,
                    accountId = 0,
                    category = category,
                    dateTime = viewModel.selectedDate.value ?: 0,
                    description = description,
                )
                viewModel.addRecord(record)

                setResult(Activity.RESULT_OK, intent)
                finish()

            }

        }


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

    private fun showSnackbar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}