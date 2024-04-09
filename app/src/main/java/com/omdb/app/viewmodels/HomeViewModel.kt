package com.omdb.app.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omdb.app.api.ResultWrapper
import com.omdb.app.dispatchers.TaskDispatchers
import com.omdb.app.models.MoviesResponse
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

    private val _searchResult = MutableLiveData<ResultWrapper<MoviesResponse>>()
    val searchResults: LiveData<ResultWrapper<MoviesResponse>>
        get() = _searchResult

    fun searchMovies(titleStr: String) {
        searchJob?.cancel()
        _searchResult.postValue(ResultWrapper.Loading())

        searchJob = viewModelScope.launch(dispatchers.io()) {
            Log.e("searching Movie","Yes VM")
            _searchResult.postValue(repository.searchMovies(titleStr))
        }
    }

}