package com.volvo.weatherlist

import com.data.Weather
import com.data.WeatherWrapper
import com.network.NetworkApi
import com.network.ResponseWrapper
import com.network.getResult
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
        when (val result =
            getResult { networkApi.getEndPoints().getWeather(city, BuildConfig.API_KEY) }) {
            is ResponseWrapper.Error.BadRequest,
            is ResponseWrapper.Error.Forbidden,
            is ResponseWrapper.Error.NoInternetConnection,
            is ResponseWrapper.Error.Unknown -> CitySearchResult.Error(city, result.toString())
            is ResponseWrapper.Empty -> CitySearchResult.NotFound
            is ResponseWrapper.Success -> {
                val weatherInfo = result.body.weather[0]
                CitySearchResult.SearchResult(
                    city,
                    weatherInfo,
                    getWeatherIconUrl.execute(weatherInfo.icon)
                )
            }
        }
    }
}

internal sealed class CitySearchResult {
    object Loading : CitySearchResult()
    data class SearchResult(
        val city: String,
        val weather: Weather,
        val weatherIconUrl: String,
    ) : CitySearchResult()

    data class Error(val city: String, val error: String) : CitySearchResult()
    object NotFound : CitySearchResult()
}