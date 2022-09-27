package com.techmave.fisherville.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.techmave.fisherville.databinding.ModelTransactionBinding
import com.techmave.fisherville.model.Transaction
import javax.inject.Inject

class TransactionAdapter @Inject constructor(): ListAdapter<Transaction, TransactionAdapter.ViewHolder>(Transaction.TransactionDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ModelTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        getItem(position)?.let { holder.bind(it) }

//        val item = items[position]
//        val text = context?.getString(R.string.transaction_text, Utility.convertToOneDecimalPoints(item.fishAmount), Utility.convertToOneDecimalPoints(item.transactionAmount))
//
//        holder.binding.transactionFishName.text = item.fishName
//        holder.binding.transactionContent.text = text

//        if (!item.fishThumb.isNullOrEmpty()) {
//
//            Picasso.get().load(item.fishThumb).into(holder.binding.transactionThumb)
//        }

//        if (item.transactionType == 0L) {
//
//            holder.binding.transactionTypeBadgeBuy.visibility = View.VISIBLE
//            holder.binding.transactionTypeBadgeSell.visibility = View.INVISIBLE
//
//        } else {
//
//            holder.binding.transactionTypeBadgeSell.visibility = View.VISIBLE
//            holder.binding.transactionTypeBadgeBuy.visibility = View.INVISIBLE
//        }
    }

    inner class ViewHolder(val binding: ModelTransactionBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {

            binding.transaction = transaction
            binding.executePendingBindings()
        }
    }
}