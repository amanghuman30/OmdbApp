package com.omdb.app.source

import com.omdb.app.api.ResultWrapper
import com.omdb.app.models.MoviesResponse

interface MovieDataSource {

    suspend fun searchMovies(str: String) : MoviesResponse

}