package com.volvo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.volvo.databinding.ActivityMainBinding
import com.volvo.weatherlist.CityWeatherAdapter
import com.volvo.weatherlist.WeatherListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherListViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val adapter by lazy { CityWeatherAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupList()
        viewModel.fetchWeather()
        observeUiState()

    }

    private fun setupList() {
        binding.swiperefresh.setOnRefreshListener { viewModel.fetchWeather() }
        binding.citiesWeather.adapter = adapter
    }

    private fun observeUiState() {
        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUiState().collect { uiState ->
                    when (uiState) {
                        WeatherListViewModel.ListUiState.Error -> {
                            hideRefresh()
                            // show error
                        }
                        WeatherListViewModel.ListUiState.Initial -> Unit
                        is WeatherListViewModel.ListUiState.Loaded -> {
                            hideRefresh()
                            adapter.updateList(uiState.weatherItemsState)
                        }
                        is WeatherListViewModel.ListUiState.Loading -> {
                            binding.swiperefresh.isRefreshing = true
                            adapter.updateList(uiState.cities)
                        }
                        WeatherListViewModel.ListUiState.Empty -> {
                            // todo show empty view
                        }
                    }
                }
            }
        }
    }

    private fun hideRefresh() {
        binding.swiperefresh.isRefreshing = false
    }
}