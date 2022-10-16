@file:OptIn(ExperimentalCoroutinesApi::class)

package com.volvo.weatherlist.domain

import com.data.mockWeatherWrapper
import com.volvo.weatherlist.data.CitySearchResult
import com.volvo.weatherlist.data.WeatherRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
internal class GetCitiesWeatherUseCaseImplTest {
    @Mock
    private lateinit var repository: WeatherRepository

    @InjectMocks
    private lateinit var useCase: GetCitiesWeatherUseCaseImpl

    @Test
    fun `execute - return map of cities with weather info - repository return result`() = runTest {
        val searchResult = CitySearchResult.SearchResult(
            "stockholm",
            mockWeatherWrapper(),
            "weather icon"
        )
        whenever(repository.getWeather(any())).thenReturn(searchResult)

        val actual = useCase.execute(listOf("stockholm"))

        assertEquals(actual, mapOf("stockholm" to searchResult))
    }

    @Test
    fun `execute - return map of loading cities - repository return result`() = runTest {
        val searchResult = CitySearchResult.Loading

        whenever(repository.getWeather(any())).thenReturn(searchResult)

        val actual = useCase.execute(listOf("stockholm"))

        assertEquals(actual, mapOf("stockholm" to searchResult))
    }

    @Test
    fun `execute - return map of cities and error - repository return error`() = runTest {
        val searchResult = CitySearchResult.Error("stockholm", "not found")

        whenever(repository.getWeather(any())).thenReturn(searchResult)

        val actual = useCase.execute(listOf("stockholm"))

        assertEquals(actual, mapOf("stockholm" to searchResult))
    }
}