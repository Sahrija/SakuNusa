package com.example.sakunusa.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sakunusa.R
import com.example.sakunusa.databinding.ItemRecordBinding
import com.example.sakunusa.model.RecordItem
import com.example.sakunusa.utils.Utils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

typealias RecordId = Int

class GroupedRecordAdapter(
    private var items: List<RecordItem>,
    private var onclick: (RecordId) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_RECORD = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is RecordItem.GroupHeader -> VIEW_TYPE_HEADER
            is RecordItem.Record -> VIEW_TYPE_RECORD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_group_header, parent, false)
            GroupHeaderViewHolder(view)
        } else {
            val binding =
                ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            RecordViewHolder(binding, parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GroupHeaderViewHolder -> {
                val header = items[position] as RecordItem.GroupHeader
                holder.bind(header)
            }

            is RecordViewHolder -> {
                val record = items[position] as RecordItem.Record
                holder.bind(record, onclick)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class GroupHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tvGroupTitle)
        private val balance: TextView = itemView.findViewById(R.id.tvBalance)

        fun bind(header: RecordItem.GroupHeader) {
            title.text = header.title
            balance.text = Utils.formatAmount(header.totalAmount)
        }
    }

    class RecordViewHolder(private val binding: ItemRecordBinding, val parent: ViewGroup) :
        RecyclerView.ViewHolder(binding.root) {
        private val category: TextView = binding.tvCategory
        private val amount: TextView = binding.tvAmount
        private val dateTime: TextView = binding.tvDateTime
        private val description: TextView = binding.tvDescription

        fun bind(record: RecordItem.Record, onclick: (RecordId) -> Unit) {
            val color = if (record.type == 0) {
                ContextCompat.getColor(parent.context, R.color.red_500)
            } else {
                ContextCompat.getColor(parent.context, R.color.green_500)
            }
            binding.tvAmount.setTextColor(color)

            category.text = record.category
            amount.text = Utils.formatAmount(record.amount)
            dateTime.text =
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(record.dateTime))
            description.text = record.description

            binding.root.setOnClickListener {
                onclick(record.id)
            }
        }
    }

    fun updateData(newData: List<RecordItem>) {
        items = newData
        notifyDataSetChanged()
    }
}
