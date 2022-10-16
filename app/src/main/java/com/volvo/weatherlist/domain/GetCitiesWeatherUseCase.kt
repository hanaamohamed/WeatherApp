package com.volvo.weatherlist.domain

import com.volvo.weatherlist.CitySearchResult
import com.volvo.weatherlist.WeatherRepository
import javax.inject.Inject

internal interface GetCitiesWeatherUseCase {
    suspend fun execute(cities: List<String>): MutableMap<String, CitySearchResult>
}

internal class GetCitiesWeatherUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository,
) : GetCitiesWeatherUseCase {

    override suspend fun execute(
        cities: List<String>
    ): MutableMap<String, CitySearchResult> {
        val citiesMap = mutableMapOf<String, CitySearchResult>()
        cities.forEach { city ->
            citiesMap[city] = repository.getWeather(city)
        }
        return citiesMap
    }
}
