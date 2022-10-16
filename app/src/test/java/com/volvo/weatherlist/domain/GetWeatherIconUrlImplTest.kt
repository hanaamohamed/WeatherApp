package com.volvo.weatherlist.domain

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class GetWeatherIconUrlImplTest {
    @InjectMocks
    private lateinit var getWeatherIconUrl: GetWeatherIconUrlImpl

    @Test
    fun `execute - return weather icon with the provided icon- always`() {
        val iconId = "icon"
        val actual = getWeatherIconUrl.execute(iconId)

        assertEquals("https://openweathermap.org/img/wn/$iconId@2x.png", actual)
    }
}