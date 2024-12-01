package com.example.sakunusa.ui.records_copy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.sakunusa.R
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.databinding.ActivityRecordsBinding
import com.example.sakunusa.ui.newrecord.NewRecordActivity

class RecordsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRecordsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_records)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

         binding.fab.setOnClickListener {
            val intent = Intent(this, NewRecordActivity::class.java)
            newRecordLauncher.launch(intent)
        }
    }

    private val newRecordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newRecord = result.data?.getParcelableExtra<RecordEntity>("new_record")
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_records)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}