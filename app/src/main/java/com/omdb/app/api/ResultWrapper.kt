package com.omdb.app.api

sealed class ResultWrapper<T>(
   val data: T? = null,
   val message: String? = null
) {
   class Success<T>(data: T) : ResultWrapper<T>(data)
   class Loading<T>(data: T? = null) : ResultWrapper<T>(data)
   class Error<T>(message: String, data: T? = null) : ResultWrapper<T>(data, message)
   class NetworkError<T>(): ResultWrapper<T>()
}