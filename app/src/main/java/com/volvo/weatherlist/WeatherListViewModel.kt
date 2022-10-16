package com.volvo.weatherlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volvo.weatherlist.data.CitySearchResult
import com.volvo.weatherlist.domain.GetCitiesListUseCase
import com.volvo.weatherlist.domain.GetCitiesWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WeatherListViewModel @Inject constructor(
    private val getCitiesListUseCase: GetCitiesListUseCase,
    private val getCitiesWeatherUseCase: GetCitiesWeatherUseCase,
) : ViewModel() {
    private var job: Job? = null

    private val listUiState = MutableStateFlow<ListUiState>(ListUiState.Initial)

    fun getUiState() = listUiState.asStateFlow()

    fun fetchWeather() {
        job?.cancel()
        job = viewModelScope.launch {
            val cities = getCitiesListUseCase.execute()
            renderCities(cities)
            loadWeather(cities)
        }
    }

    private suspend fun loadWeather(cities: List<String>) {
        if (cities.isEmpty()) {
            listUiState.value = ListUiState.Empty
            return
        }
        val list = mutableListOf<WeatherItemState>()
        val citiesWeatherMap = getCitiesWeatherUseCase.execute(cities)
        citiesWeatherMap.forEach { cityResult ->
            list.add(cityResult.value.mapToUiState(cityResult.key))
        }
        listUiState.value = if (list.isNotEmpty()) {
            ListUiState.Loaded(list)
        } else {
            ListUiState.Error
        }
    }

    private fun renderCities(cities: List<String>) {
        val loadingList = cities.map { city -> WeatherItemState.Loading(city) }
        listUiState.value = ListUiState.Loading(loadingList)
    }

    /**
     * Ui state of the list of cities list with their weather.
     */
    sealed class ListUiState {
        object Initial : ListUiState()

        object Empty : ListUiState()

        /**
         * Loading state with just list of cities and no weather information.
         */
        data class Loading(val cities: List<WeatherItemState.Loading>) : ListUiState()

        /**
         * List of [WeatherItemState]
         */
        data class Loaded(val weatherItemsState: List<WeatherItemState>) : ListUiState()

        /**
         * Something went wrong while loading the weather information.
         */
        object Error : ListUiState()
    }

    sealed class WeatherItemState {
        abstract val city: String

        data class Loading(override val city: String) : WeatherItemState()
        data class Loaded(
            override val city: String, val result: CitySearchResult.SearchResult,
        ) : WeatherItemState()

        data class Error(override val city: String, val error: String) : WeatherItemState()
        data class NotFound(override val city: String) : WeatherItemState()
    }

    private fun CitySearchResult.mapToUiState(city: String): WeatherItemState = when (this) {
        is CitySearchResult.Error -> WeatherItemState.Error(city, error)
        CitySearchResult.Loading -> WeatherItemState.Loading(city)
        is CitySearchResult.SearchResult -> WeatherItemState.Loaded(city, this)
        CitySearchResult.NotFound -> WeatherItemState.NotFound(city)
    }
}