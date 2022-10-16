package com.volvo.weatherlist.domain

import javax.inject.Inject

internal interface GetWeatherIconUrl {
    fun execute(iconId: String): String
}

internal class GetWeatherIconUrlImpl @Inject constructor() : GetWeatherIconUrl {
    override fun execute(iconId: String): String {
        return "https://openweathermap.org/img/wn/$iconId@2x.png"
    }
}