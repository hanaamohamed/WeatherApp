package com.volvo.weatherlist.domain

import javax.inject.Inject

internal interface GetCitiesListUseCase {
    fun execute(): List<String>
}

internal class GetCitiesListUseCaseImpl @Inject constructor() : GetCitiesListUseCase {
    override fun execute(): List<String> {
        val cities = mutableListOf<String>()
        Cities.values().forEach {
            cities.add(it.cityName)
        }
        return cities
    }
}

enum class Cities(val cityName: String) {
    Gothenburg("Gothenburg"),
    Stockholm("Stockholm"),
    MountainView("Mountain View"),
    London("London"),
    NewYork("New York"),
    Berlin("Berlin"),
}