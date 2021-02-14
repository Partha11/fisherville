package com.techmave.fisherville.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.ModelNewsBinding
import com.techmave.fisherville.model.News

class NewsAdapter(context: Context): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var items = mutableListOf<News>()
    private var listener: NewsInteractionListener? = null

    init {

        if (context is NewsInteractionListener) {

            listener = context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.model_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.binding.newsTitle.text = item.title
        holder.binding.newsContent.text = item.content

        if (item.timestamp < (System.currentTimeMillis() - (60 * 60 * 24 * 1000))) {

            holder.binding.newsBadge.visibility = View.GONE

        } else {

            holder.binding.newsBadge.visibility = View.VISIBLE
        }

        if (!item.thumbnail.isNullOrEmpty()) {

            Picasso.get().load(item.thumbnail)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.binding.newsThumb, object: Callback {

                        override fun onSuccess() {

                            Log.d("News:Picasso", "Successfully Loaded")
                        }

                        override fun onError(e: Exception?) {

                            Log.d("News:Picasso", e?.localizedMessage, e)
                        }
                    })
        }
    }

    override fun getItemCount(): Int {

        return items.size
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        var binding: ModelNewsBinding = ModelNewsBinding.bind(view)

        init {

            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            listener?.onNewsClicked(items[adapterPosition])
        }
    }

    interface NewsInteractionListener {

        fun onNewsClicked(news: News)
    }
}