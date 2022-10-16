@file:OptIn(ExperimentalCoroutinesApi::class)

package com.volvo.weatherlist

import com.data.WeatherWrapper
import com.data.mockWeatherWrapper
import com.network.NetworkApi
import com.network.ResponseWrapper
import com.volvo.weatherlist.domain.GetWeatherIconUrl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import javax.inject.Inject

@RunWith(MockitoJUnitRunner::class)
internal class WeatherRepositoryImplTest {

    @Mock
    private lateinit var networkApi: NetworkApi

    @Mock
    private lateinit var getWeatherIconUrl: GetWeatherIconUrl


    @Inject
    private lateinit var repository: WeatherRepository

    private val city = "Stockholm"


    @Before
    fun setup() {
        repository = WeatherRepositoryImpl(networkApi, Dispatchers.Unconfined, getWeatherIconUrl)
    }

    @Test
    fun `getWeather - return not found - response is null`() = runTest {
        whenever(networkApi.getWeather(any(), any())).thenReturn(ResponseWrapper.Empty())

        val actual = repository.getWeather(city)

        assertEquals(actual, CitySearchResult.NotFound)
    }

    @Test
    fun `getWeather - return Forbidden - response error code is forbidden`() = runTest {
        val result = ResponseWrapper.Error.Forbidden<WeatherWrapper>(303)
        whenever(networkApi.getWeather(any(), any())).thenReturn(result)

        val actual = repository.getWeather(city)

        assertEquals(actual, CitySearchResult.Error(city, result.toString()))
    }

    @Test
    fun `getWeather - return search result with icon - response is found`() = runTest {
        val weatherWrapper = mockWeatherWrapper()
        whenever(networkApi.getWeather(any(), any())).thenReturn(
            ResponseWrapper.Success(
                weatherWrapper
            )
        )
        whenever(getWeatherIconUrl.execute(any())).thenReturn("icon")

        val actual = repository.getWeather("Stockholm")

        assertEquals(
            actual,
            CitySearchResult.SearchResult("Stockholm", weatherWrapper, weatherIconUrl = "icon")
        )
    }
}