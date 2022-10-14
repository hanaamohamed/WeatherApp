package com.volvo.weatherlist

import com.data.WeatherWrapper
import com.network.NetworkApi
import com.network.ResponseWrapper
import com.network.getResult
import com.volvo.BuildConfig
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

internal interface WeatherRepository {
    suspend fun getWeather(city: String): WeatherWrapper?
}

internal class WeatherRepositoryImpl @Inject constructor(
    private val networkApi: NetworkApi,
    private val ioContext: CoroutineContext
) : WeatherRepository {

    override suspend fun getWeather(city: String) = withContext(ioContext) {
        when (val result =
            getResult { networkApi.getEndPoints().getWeather(city, BuildConfig.API_KEY) }) {
            is ResponseWrapper.Empty,
            is ResponseWrapper.Error.BadRequest,
            is ResponseWrapper.Error.Forbidden,
            is ResponseWrapper.Error.NoInternetConnection,
            is ResponseWrapper.Error.Unknown -> null
            is ResponseWrapper.Success -> result.body
        }
    }
}