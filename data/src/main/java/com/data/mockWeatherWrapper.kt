package com.data

fun mockWeatherWrapper(
    id: Int = 123,
    description: String = "Rainy",
    icon: String = "12d",
    temp: Float = 123f,
    feelsLike: Float = 123f,
) = WeatherWrapper(
    weather = listOf(
        Weather(
            id = id,
            description = description,
            icon = icon
        )
    ),
    MainWeather(temp, feelsLike)
)
