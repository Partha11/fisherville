package com.techmave.fisherville.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.ActivityNewsBinding
import com.techmave.fisherville.model.News
import com.techmave.fisherville.util.Constants
import com.techmave.fisherville.util.Utility

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {

        val news: News?

        if (intent.hasExtra(Constants.INTENT_DATA_NEWS)) {

            val temp = intent.getStringExtra(Constants.INTENT_DATA_NEWS)

            if (!temp.isNullOrEmpty()) {

                news = Gson().fromJson(temp, News::class.java)
                updateUi(news)
            }
        }

        setupCollapsingToolbar()
    }

    private fun updateUi(news: News?) {

        val date = Utility.getDateFromTimestamp(news?.timestamp ?: 0)

        binding.newsTitle.text = news?.title
        binding.newsContent.text = news?.content
        binding.newsTime.text = date

        Picasso.get().load(news?.thumbnail)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.newsThumb, object: Callback {

                    override fun onSuccess() {

                        Log.d("News:Picasso", "Successfully Loaded")
                    }

                    override fun onError(e: Exception?) {

                        Log.d("News:Picasso", e?.localizedMessage, e)
                    }
                })
    }

    private fun setupCollapsingToolbar() {

        binding.appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {

            var isVisible = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {

                if (scrollRange == -1) scrollRange = appBarLayout.totalScrollRange

                if (scrollRange + verticalOffset == 0) {

                    binding.collapsingToolbar.title = "News"
                    isVisible = true

                } else if (isVisible) {

                    binding.collapsingToolbar.title = " "
                    isVisible = false
                }
            }
        })
    }
}