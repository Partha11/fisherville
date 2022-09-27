package com.techmave.fisherville.model

import androidx.recyclerview.widget.DiffUtil

class News {

    var id: String = ""
    var title: String? = ""
    var content: String? = ""
    var thumbnail: String? = ""
    var timestamp: Long = 0
    var clickCount: Long = 0

    var showBadge: Boolean = timestamp >= System.currentTimeMillis() - (60 * 60 * 24 * 1000)

    override fun equals(other: Any?) = other is News && other.id == this.id && other.timestamp == this.timestamp

    override fun hashCode(): Int {

        var result = id.hashCode()

        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (thumbnail?.hashCode() ?: 0)
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + clickCount.hashCode()

        return result
    }

    object NewsDiffUtil: DiffUtil.ItemCallback<News>() {

        override fun areItemsTheSame(oldItem: News, newItem: News) = oldItem.id == newItem.id && oldItem.timestamp == newItem.timestamp

        override fun areContentsTheSame(oldItem: News, newItem: News) = oldItem == newItem
    }
}