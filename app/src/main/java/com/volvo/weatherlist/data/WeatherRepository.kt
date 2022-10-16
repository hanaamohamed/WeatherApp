package com.volvo.weatherlist.data

import com.data.WeatherWrapper
import com.network.NetworkApi
import com.network.ResponseWrapper
import com.volvo.BuildConfig
import com.volvo.weatherlist.domain.GetWeatherIconUrl
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

internal interface WeatherRepository {
    suspend fun getWeather(city: String): CitySearchResult
}

internal class WeatherRepositoryImpl @Inject constructor(
    private val networkApi: NetworkApi,
    private val ioContext: CoroutineContext,
    private val getWeatherIconUrl: GetWeatherIconUrl
) : WeatherRepository {

    override suspend fun getWeather(city: String) = withContext(ioContext) {
        when (val result = networkApi.getWeather(city, BuildConfig.API_KEY)) {
            is ResponseWrapper.Error.BadRequest,
            is ResponseWrapper.Error.Forbidden,
            is ResponseWrapper.Error.NoInternetConnection,
            is ResponseWrapper.Error.Unknown -> CitySearchResult.Error(city, result.toString())
            is ResponseWrapper.Empty -> CitySearchResult.NotFound
            is ResponseWrapper.Success -> {
                val weatherWrapper = result.body
                CitySearchResult.SearchResult(
                    city,
                    weatherWrapper,
                    getWeatherIconUrl.execute(weatherWrapper.weather[0].icon)
                )
            }
        }
    }
}

internal sealed class CitySearchResult {
    object Loading : CitySearchResult()
    data class SearchResult(
        val city: String,
        val result: WeatherWrapper,
        val weatherIconUrl: String,
    ) : CitySearchResult()

    data class Error(val city: String, val error: String) : CitySearchResult()
    object NotFound : CitySearchResult()
}