package com.techmave.fisherville.view.fragment.dashboard.news

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.techmave.fisherville.databinding.FragmentNewsDetailsBinding
import com.techmave.fisherville.model.News
import com.techmave.fisherville.view.fragment.BaseFragment
import com.techmave.fisherville.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailsFragment @Inject constructor(): BaseFragment<FragmentNewsDetailsBinding, NewsViewModel>(FragmentNewsDetailsBinding::inflate) {

    override val viewModel: NewsViewModel by viewModels()

    private val args: NewsDetailsFragmentArgs by navArgs()

    override fun initialize() {

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        args.news?.let {

            gson.fromJson(it, News::class.java)?.let { news -> viewModel.singleNews.postValue(news) }
        }
    }
}