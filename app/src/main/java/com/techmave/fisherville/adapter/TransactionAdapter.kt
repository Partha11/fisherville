package com.techmave.fisherville.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.ModelTransactionBinding
import com.techmave.fisherville.model.Transaction
import com.techmave.fisherville.util.Utility

class TransactionAdapter(private val context: Context?): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    var items = mutableListOf<Transaction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.model_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]
        val text = context?.getString(R.string.transaction_text, Utility.convertToOneDecimalPoints(item.fishAmount), Utility.convertToOneDecimalPoints(item.transactionAmount))

        holder.binding.transactionFishName.text = item.fishName
        holder.binding.transactionContent.text = text

        if (!item.fishThumb.isNullOrEmpty()) {

            Picasso.get().load(item.fishThumb).into(holder.binding.transactionThumb)
        }

        if (item.transactionType == 0L) {

            holder.binding.transactionTypeBadgeBuy.visibility = View.VISIBLE
            holder.binding.transactionTypeBadgeSell.visibility = View.INVISIBLE

        } else {

            holder.binding.transactionTypeBadgeSell.visibility = View.VISIBLE
            holder.binding.transactionTypeBadgeBuy.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {

        return items.size
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var binding: ModelTransactionBinding = ModelTransactionBinding.bind(view)
    }
}