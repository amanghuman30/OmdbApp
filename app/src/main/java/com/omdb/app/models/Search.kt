package com.omdb.app.models

data class Search(
    val Poster: String? = null,
    val Title: String? = null,
    val Type: String? = null,
    val Year: String? = null,
    val imdbID: String
)