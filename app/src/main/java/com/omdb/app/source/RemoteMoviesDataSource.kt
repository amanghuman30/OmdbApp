package com.omdb.app.source

import android.util.Log
import com.omdb.app.BuildConfig
import com.omdb.app.api.MovieSearchApi
import com.omdb.app.models.MoviesResponse
import javax.inject.Singleton

@Singleton
class RemoteMoviesDataSource(private val searchClient: MovieSearchApi) : MovieDataSource{
    override suspend fun searchMovies(str: String): MoviesResponse {
        Log.e("searching Movie","Yes remote data store")
        return searchClient.searchMovies(str, BuildConfig.API_KEY)
    }

}