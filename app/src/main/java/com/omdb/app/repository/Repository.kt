package com.omdb.app.repository

import com.omdb.app.api.ResultWrapper
import com.omdb.app.models.MoviesResponse

interface Repository {

    suspend fun searchMovies(str: String): ResultWrapper<MoviesResponse>

}