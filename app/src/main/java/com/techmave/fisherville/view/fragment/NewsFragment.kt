package com.techmave.fisherville.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.techmave.fisherville.adapter.NewsAdapter
import com.techmave.fisherville.databinding.FragmentNewsBinding
import com.techmave.fisherville.listener.FragmentListener
import com.techmave.fisherville.util.SharedPrefs
import com.techmave.fisherville.util.Utility

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    private var prefs: SharedPrefs? = null
    private var adapter: NewsAdapter? = null
    private var listener: FragmentListener? = null

    companion object {

        @JvmStatic
        fun getInstance() = NewsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentNewsBinding.inflate(inflater)
        return binding.root
    }

    override fun onAttach(context: Context) {

        super.onAttach(context)
        (activity as AppCompatActivity).supportActionBar?.hide()

        if (context is FragmentListener) {

            listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {

        adapter = NewsAdapter()
        prefs = SharedPrefs(requireContext())

//        binding.collapsingToolbar.title = "News"
//
//        binding.collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
//        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.rgb(255, 255, 255))

        setupCollapsingToolbar()
        setupRecyclerView()
        updateUi()
    }

    private fun setupRecyclerView() {

        binding.newsRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.newsRecycler.itemAnimator = DefaultItemAnimator()
        binding.newsRecycler.adapter = adapter
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

    private fun updateUi() {

        val greetings = "${Utility.getGreetingMessage()}, ${prefs?.userName}"

        binding.newsWelcomeText.text = greetings
    }
}