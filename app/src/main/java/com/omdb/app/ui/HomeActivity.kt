package com.omdb.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.omdb.app.databinding.ActivityHomeBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {

        binding.movieSv.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.trim()?.let { text ->
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