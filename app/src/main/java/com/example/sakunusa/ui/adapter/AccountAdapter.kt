package com.example.sakunusa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sakunusa.R
import com.example.sakunusa.data.local.entity.AccountEntity
import com.example.sakunusa.databinding.ItemAccountBinding

class AccountAdapter(
    private var onClick: (AccountEntity) -> Unit,
    private var onLongClick: (AccountEntity) -> Unit,
) :
    ListAdapter<AccountEntity, AccountAdapter.AccountViewHolder>(DIFF_CALLBACK) {
    class AccountViewHolder(
        private val binding: ItemAccountBinding,
        var onClick: (AccountEntity) -> Unit,
        var onLongClick: (AccountEntity) -> Unit,
    ) : ViewHolder(binding.root) {
        fun bind(accountEntity: AccountEntity) {
            binding.root.setBackgroundResource(
                if (accountEntity.isSelected) R.drawable.rounded_background else R.drawable.rounded_background_dashed
            )

            binding.tvName.text = accountEntity.name
            binding.tvBalance.text = accountEntity.startingAmount.toString()

            binding.root.setOnClickListener { onClick(accountEntity) }
            binding.root.setOnLongClickListener {
                onLongClick(accountEntity)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding, onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AccountEntity>() {
            override fun areItemsTheSame(oldItem: AccountEntity, newItem: AccountEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AccountEntity,
                newItem: AccountEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

