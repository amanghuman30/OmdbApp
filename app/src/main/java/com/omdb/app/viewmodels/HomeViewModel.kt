package com.omdb.app.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omdb.app.dispatchers.TaskDispatchers
import com.omdb.app.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatchers: TaskDispatchers
): ViewModel() {

    private var searchJob: Job? = null

    fun searchMovies(titleStr: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch(dispatchers.io()) {
            Log.e("searching Movie","Yes VM")
            repository.searchMovies(titleStr)
        }
    }


}