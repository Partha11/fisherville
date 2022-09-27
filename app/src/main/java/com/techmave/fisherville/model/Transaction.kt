package com.techmave.fisherville.model

import androidx.recyclerview.widget.DiffUtil

class Transaction {

    var id: String = ""
    var fishId: String = ""
    var fishName: String = ""
    var fishThumb: String? = ""
    var fishAmount: Float = 0f
    var transactionAmount: Float = 0f
    var transactionType: Long = 0
    var transactionTime: Long = 0

    override fun equals(other: Any?) = other is Transaction && other.id == this.id && other.fishId == this.fishId

    override fun hashCode(): Int {

        var result = id.hashCode()

        result = 31 * result + fishId.hashCode()
        result = 31 * result + fishName.hashCode()
        result = 31 * result + (fishThumb?.hashCode() ?: 0)
        result = 31 * result + fishAmount.hashCode()
        result = 31 * result + transactionAmount.hashCode()
        result = 31 * result + transactionType.hashCode()
        result = 31 * result + transactionTime.hashCode()

        return result
    }

    object TransactionDiffUtil: DiffUtil.ItemCallback<Transaction>() {

        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) = oldItem.id == newItem.id && oldItem.fishId == newItem.fishId

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) = oldItem == newItem
    }
}