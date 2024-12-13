package com.example.sakunusa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sakunusa.R
import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.databinding.ItemRecordBinding
import com.example.sakunusa.utils.Utils
import com.example.sakunusa.utils.Utils.formatDate

class RecordAdapter(private var onclick: (RecordEntity) -> Unit) :
    ListAdapter<RecordEntity, RecordAdapter.MyViewHolder>(RecordDiffCallback) {


    class MyViewHolder(
        private val binding: ItemRecordBinding,
        var parent: ViewGroup,
        var onclick: (RecordEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: RecordEntity) {
            val color = if (record.type == 0) {
                ContextCompat.getColor(parent.context, R.color.red_500)
            } else {
                ContextCompat.getColor(parent.context, R.color.green_500)
            }
            binding.tvAmount.setTextColor(color)


            binding.tvAmount.text = Utils.formatAmount(record.amount)
            binding.tvDateTime.text = formatDate(record.dateTime)
            binding.tvCategory.text = record.category
            binding.tvDescription.text = record.description.toString()

            binding.root.setOnClickListener { onclick(record) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, parent, onclick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val record = getItem(position)
        holder.bind(record)
    }

    object RecordDiffCallback : DiffUtil.ItemCallback<RecordEntity>() {
        override fun areItemsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
            return oldItem == newItem
        }
    }
}