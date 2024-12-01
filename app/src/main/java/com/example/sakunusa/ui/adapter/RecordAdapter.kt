package com.example.sakunusa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.databinding.RecordItemBinding
import com.example.sakunusa.utils.Utils.formatDate
import java.text.NumberFormat
import java.util.Locale

class RecordAdapter : ListAdapter<RecordEntity, RecordAdapter.MyViewHolder>(RecordDiffCallback) {


    class MyViewHolder(private val binding: RecordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(record: RecordEntity) {
            val formatter = NumberFormat.getInstance(Locale.GERMANY)
            binding.tvAmount.text = formatter.format(record.amount)
            binding.tvDateTime.text = formatDate(record.dateTime)
            binding.tvCategory.text = record.category
            binding.tvDescription.text = record.description.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val record = getItem(position)
        holder.bind(record)
    }

    object RecordDiffCallback : DiffUtil.ItemCallback<RecordEntity>() {
        override fun areItemsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
            return oldItem.id == newItem.id // Assuming 'id' is the unique identifier
        }

        override fun areContentsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
            return oldItem == newItem
        }
    }
}