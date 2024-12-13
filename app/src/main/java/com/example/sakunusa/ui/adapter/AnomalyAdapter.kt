package com.example.sakunusa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sakunusa.R
import com.example.sakunusa.data.local.entity.AnomalyWithRecord
import com.example.sakunusa.databinding.ItemAnomalyBinding
import com.example.sakunusa.utils.Utils

class AnomalyAdapter(
    private var onClick: (AnomalyWithRecord) -> Unit,
    private var onLongClick: (AnomalyWithRecord) -> Unit,
) :
    ListAdapter<AnomalyWithRecord, AnomalyAdapter.AnomalyViewHolder>(AnomalyDiffCallback) {
    class AnomalyViewHolder(
        private val binding: ItemAnomalyBinding,
        var parent: ViewGroup,
        private var onClick: (AnomalyWithRecord) -> Unit,
        private var onLongClick: (AnomalyWithRecord) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(anomalyWithRecord: AnomalyWithRecord) {
            with(binding) {
                val anomaly = anomalyWithRecord.anomaly
                val record = anomalyWithRecord.record
                tvAmount.text = Utils.formatAmount(record?.amount ?: 0)
                tvCategory.text = record?.category
                tvDate.text = Utils.formatDate(record?.dateTime ?: 0)
                tvDescription.text = record?.description
                tvLoss.text = anomaly.loss.toString()

                root.setOnClickListener { onClick(anomalyWithRecord) }
                root.setOnLongClickListener {
                    onLongClick(anomalyWithRecord)
                    true
                }

                root.setBackgroundResource(
                    if (anomaly.anomalyDetected) R.drawable.rounded_background_primary else R.drawable.rounded_background_dashed_primary
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnomalyViewHolder {
        val binding = ItemAnomalyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnomalyViewHolder(binding, parent, onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: AnomalyViewHolder, position: Int) {
        val anomalyWithRecord = getItem(position)
        holder.bind(anomalyWithRecord)
    }

    object AnomalyDiffCallback : DiffUtil.ItemCallback<AnomalyWithRecord>() {
        override fun areItemsTheSame(
            oldItem: AnomalyWithRecord,
            newItem: AnomalyWithRecord
        ): Boolean {
            return oldItem.anomaly.id == newItem.anomaly.id
        }

        override fun areContentsTheSame(
            oldItem: AnomalyWithRecord,
            newItem: AnomalyWithRecord
        ): Boolean {
            return oldItem == newItem
        }
    }
}