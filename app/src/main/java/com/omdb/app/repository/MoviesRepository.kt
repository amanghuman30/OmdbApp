package com.omdb.app.repository

import android.util.Log
import com.omdb.app.api.ResultWrapper
import com.omdb.app.models.MoviesResponse
import com.omdb.app.source.MovieDataSource
import com.omdb.app.util.NoInternetException

class MoviesRepository(private val dataSource: MovieDataSource) : Repository {

    override suspend fun searchMovies(str: String): ResultWrapper<MoviesResponse> {
        return apiRequest {
            Log.e("searching Movie","Yes Repo")
            dataSource.searchMovies(str)
        }
    }


    private suspend fun <T> apiRequest(apiCall: suspend () -> T): ResultWrapper<T> {
        return try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is NoInternetException -> {
                        ResultWrapper.NetworkError()
                    }
                    else -> {
                        ResultWrapper.Error( "Unknown error", null)
                    }
                }
            }
    }

}