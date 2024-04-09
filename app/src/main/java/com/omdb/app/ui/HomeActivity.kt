package com.omdb.app.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.omdb.app.R
import com.omdb.app.api.ResultWrapper
import com.omdb.app.databinding.ActivityHomeBinding
import com.omdb.app.models.MoviesResponse
import com.omdb.app.ui.adapters.MoviesSearchAdapter
import com.omdb.app.util.gone
import com.omdb.app.util.hideSoftKeyBoard
import com.omdb.app.util.visible
import com.omdb.app.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel : HomeViewModel by viewModels()
    private var searchQueryFlow: MutableStateFlow<String>? = null
    private val SEARCH_QUERY_DEBOUNCE_TIME: Long = 100
    private lateinit var moviesSearchAdapter: MoviesSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {

        binding.apply {
            movieSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.trim()?.takeIf { it.isNotEmpty() }?.let { text ->
                        lifecycleScope.launch {
                            searchQueryFlow?.let {
                                searchQueryFlow?.emit(text)
                            } ?: run {
                                searchQueryFlow = MutableStateFlow(text)
                                setupSearchQueryFlow()
                            }
                        }
                    }
                    return true
                }
            })

            moviesSearchAdapter = MoviesSearchAdapter()

            searchMoviesRV.apply {
                layoutManager = LinearLayoutManager(this@HomeActivity)
                adapter = moviesSearchAdapter

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy != 0)
                            recyclerView.hideSoftKeyBoard()
                    }
                })

                val divider = MaterialDividerItemDecoration(this@HomeActivity, LinearLayoutManager.VERTICAL).apply {
                    val dimen = context.resources.getDimension(R.dimen.divider_margin).toInt()
                    dividerInsetStart = dimen
                    dividerInsetEnd = dimen
                    dividerThickness = context.resources.getDimension(R.dimen.divider_thickness).toInt()
                    dividerColor = ContextCompat.getColor(this@HomeActivity,R.color.grey_gen)
                }
                addItemDecoration(divider)
            }
        }
    }

    private fun setupListeners() {

        viewModel.searchResults.observe(this) {
            handleSearchResult(it)
        }

    }

    private fun handleSearchResult(response : ResultWrapper<MoviesResponse>) {

        binding.apply {

            moviesPb.gone()

            when (response) {
                is ResultWrapper.Loading -> {
                    moviesPb.visible()
                }
                is ResultWrapper.Success -> {

                    response.data?.Error?.let {
                        showEmptyErrorView()
                        emptyErrorTv.text = it
                    } ?: run {
                        showMoviesRecycler()
                        //set recycler data here
                        response.data?.Search?.let {
                            moviesSearchAdapter.differ.submitList(it)
                        }
                    }
                }

                is ResultWrapper.NetworkError -> {
                    showEmptyErrorView()
                    emptyErrorTv.text = getString(R.string.msg_no_internet)
                }

                is ResultWrapper.Error -> {
                    showEmptyErrorView()
                    emptyErrorTv.text = getString(R.string.something_went_wrong_error)
                }
            }
        }

    }

    private fun showEmptyErrorView() {

        binding.apply {
            searchMoviesRV.gone()
            searchIv.visible()
            emptyErrorTv.visible()
        }
    }

    private fun showMoviesRecycler() {
        binding.apply {
            searchMoviesRV.visible()
            searchIv.gone()
            emptyErrorTv.gone()
        }
    }

    /**
     * Method listens to the changes through the flow for
     * debouncing and distinct queries to avoid multiple calls to the api
     * query changes will be ignored for the debounce time and
     * api will be called once the debounce time has passed
     */
    @OptIn(FlowPreview::class)
    private fun setupSearchQueryFlow() {
        lifecycleScope.launch(Dispatchers.IO) {
            searchQueryFlow
                ?.debounce(SEARCH_QUERY_DEBOUNCE_TIME)
                ?.distinctUntilChanged()
                ?.collect {
                    viewModel.searchMovies(it)
                }
        }
    }

}