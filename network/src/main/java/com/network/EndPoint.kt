package com.network

import com.data.WeatherWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EndPoint {
    @GET("weather/")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") appId: String
    ): Response<WeatherWrapper>
}