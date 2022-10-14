package com.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

interface NetworkApi {
    fun getEndPoints(): EndPoint
}

internal class NetworkApiImpl @Inject constructor() : NetworkApi {
    private val retrofitClient by lazy { getClient() }

    private fun getClient(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    override fun getEndPoints(): EndPoint {
        return retrofitClient.create(EndPoint::class.java)
    }
}