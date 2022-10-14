package com.volvo.weatherlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WeatherListViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {
    private val uiState = MutableStateFlow<UiState>(UiState.Initial)

    fun getUiState() = uiState.asStateFlow()

    fun onAttach() {
        viewModelScope.launch {
            val weather = repository.getWeather("Stockholm")
            uiState.value = weather?.let { UiState.Loaded(it.weather[0]) } ?: UiState.Empty
        }
    }

    sealed class UiState {
        object Initial : UiState()
        data class Loaded(val weather: Weather) : UiState()
        object Empty : UiState()
    }
}