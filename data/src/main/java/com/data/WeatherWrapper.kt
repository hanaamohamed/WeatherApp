package com.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherWrapper(
    val weather: List<Weather>,
    val main: MainWeather,
) : Parcelable

@Parcelize
data class Weather(
    val id: Int,
    val description: String,
    val icon: String
) : Parcelable

@Parcelize
data class MainWeather(
    val temp: Float,
    val feels_like: Float,
) : Parcelable

fun Float.mapToCelsius() = this - 273.15