package com.omdb.app.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface TaskDispatchers {

    fun io() : CoroutineDispatcher
    fun main() : CoroutineDispatcher
    fun default() : CoroutineDispatcher
    fun unconfined() : CoroutineDispatcher

}