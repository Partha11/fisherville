package com.techmave.fisherville.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.ModelNewsBinding
import com.techmave.fisherville.model.News

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var items: List<News>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.model_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {

        return items?.size ?: 6
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var binding: ModelNewsBinding = ModelNewsBinding.bind(view)
    }
}