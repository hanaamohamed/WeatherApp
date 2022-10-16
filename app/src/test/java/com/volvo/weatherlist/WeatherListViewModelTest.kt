@file:OptIn(ExperimentalCoroutinesApi::class)

package com.volvo.weatherlist

import com.data.mockWeatherWrapper
import com.volvo.weatherlist.domain.GetCitiesListUseCase
import com.volvo.weatherlist.domain.GetCitiesWeatherUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
internal class WeatherListViewModelTest {
    @Mock
    private lateinit var getCitiesListUseCase: GetCitiesListUseCase

    @Mock
    private lateinit var getCitiesWeatherUseCase: GetCitiesWeatherUseCase

    @InjectMocks
    private lateinit var viewModel: WeatherListViewModel

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val city = "Stockholm"

    @Test
    fun `fetchWeather - set ui state to empty - no cities returned`() {
        whenever(getCitiesListUseCase.execute()).thenReturn(emptyList())

        viewModel.fetchWeather()

        assertEquals(WeatherListViewModel.ListUiState.Empty, viewModel.getUiState().value)
    }

    @Test
    fun `fetchWeather - set ui state to error - get weather info return empty`() = runTest {
        whenever(getCitiesListUseCase.execute()).thenReturn(listOf(city))
        whenever(getCitiesWeatherUseCase.execute(any())).thenReturn(mapOf())

        viewModel.fetchWeather()

        assertEquals(WeatherListViewModel.ListUiState.Error, viewModel.getUiState().value)
    }

    @Test
    fun `fetchWeather - set ui state to loaded -  cities returned and weather info fetched successfully`() =
        runTest {
            val searchResult = CitySearchResult.SearchResult(city, mockWeatherWrapper(), "icon")
            whenever(getCitiesListUseCase.execute()).thenReturn(listOf(city))
            whenever(getCitiesWeatherUseCase.execute(listOf(city))).thenReturn(
                mapOf(city to searchResult)
            )

            viewModel.fetchWeather()

            assertEquals(
                WeatherListViewModel.ListUiState.Loaded(
                    listOf(
                        WeatherListViewModel.WeatherItemState.Loaded(
                            city,
                            searchResult
                        )
                    )
                ), viewModel.getUiState().value
            )
        }
}