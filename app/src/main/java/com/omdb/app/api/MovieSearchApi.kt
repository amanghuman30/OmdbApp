package com.omdb.app.api

import com.omdb.app.models.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieSearchApi {
    @GET(".")
    suspend fun searchMovies(
        @Query("s") searchStr: String,
        @Query("apikey") apiKey: String
        ): MoviesResponse

}