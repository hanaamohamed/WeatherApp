package com.network

import com.data.WeatherWrapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

interface NetworkApi {
    suspend fun getWeather(city: String, appId: String): Response<WeatherWrapper>
}

internal class NetworkApiImpl @Inject constructor() : NetworkApi {
    private val retrofitClient by lazy { getClient() }
    private val endPoint by lazy { retrofitClient.create(EndPoint::class.java) }

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

    override suspend fun getWeather(city: String, appId: String): Response<WeatherWrapper> {
        return endPoint.getWeather(city, appId)
    }
}