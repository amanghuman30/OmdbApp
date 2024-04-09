package com.omdb.app.models

data class MoviesResponse(
    val Search: List<Search>,
    val totalResults: Int? = 0,
    val Response: Boolean,
    val Error: String? = null
)