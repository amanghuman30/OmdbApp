package com.omdb.app.api

import com.omdb.app.models.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieSearchApi {
    @GET
    suspend fun searchMovies(
        @Path("s") searchStr: String,
        @Path("apikey") apiKey: String
        ): Resource<MoviesResponse>

}