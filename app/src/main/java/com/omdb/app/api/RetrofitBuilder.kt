package com.omdb.app.api

import com.omdb.app.util.NetworkHelper
import com.omdb.app.util.NoInternetException
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitBuilder @Inject constructor(private val networkHelper: NetworkHelper) {


    private val retrofit by lazy {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(4, TimeUnit.MINUTES)
            .addInterceptor(logging)
            .addInterceptor(NetworkConnectionInterceptor(networkHelper))
            .build()

        Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    class NetworkConnectionInterceptor(private val netHelper: NetworkHelper) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            if(!netHelper.isNetworkConnected())  {
                throw NoInternetException("")
            }
            return chain.proceed(chain.request())
        }
    }

}