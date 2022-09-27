package com.techmave.fisherville.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.techmave.fisherville.databinding.ModelNewsBinding
import com.techmave.fisherville.model.News
import javax.inject.Inject

class NewsAdapter @Inject constructor(): ListAdapter<News, NewsAdapter.ViewHolder>(News.NewsDiffUtil) {

    var listener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ModelNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {

        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(val binding: ModelNewsBinding): RecyclerView.ViewHolder(binding.root) {

        init {

            binding.listener = listener
        }

        fun bind(news: News) {

            binding.news = news
            binding.executePendingBindings()
        }
    }

    fun interface OnClickListener {

        fun onClicked(news: News?)
    }
}