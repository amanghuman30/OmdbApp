package com.omdb.app.dispatchers

import kotlinx.coroutines.Dispatchers

class MoviesTaskDispatchers : TaskDispatchers{
    override fun io() = Dispatchers.IO

    override fun main() = Dispatchers.Main

    override fun default() = Dispatchers.Default

    override fun unconfined() = Dispatchers.Unconfined
}