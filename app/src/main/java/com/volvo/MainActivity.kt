package com.volvo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.volvo.databinding.ActivityMainBinding
import com.volvo.weatherlist.WeatherListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherListViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.onAttach()
        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUiState().collect { uiState ->
                    when (uiState) {
                        WeatherListViewModel.UiState.Empty -> {}
                        WeatherListViewModel.UiState.Initial -> Unit
                        is WeatherListViewModel.UiState.Loaded -> {
                            binding.weatherStatus.text = uiState.weather.description
                        }
                    }
                }
            }
        }
    }
}