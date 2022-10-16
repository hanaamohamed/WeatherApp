package com.volvo.weatherlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.data.mapToCelsius
import com.volvo.R
import com.volvo.databinding.ListItemWeatherBinding

internal class CityWeatherAdapter : Adapter<CityWeatherViewHolder>() {
    private val list: MutableList<WeatherListViewModel.WeatherItemState> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityWeatherViewHolder {
        return CityWeatherViewHolder(
            ListItemWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CityWeatherViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<WeatherListViewModel.WeatherItemState>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}

internal class CityWeatherViewHolder(private val binding: ListItemWeatherBinding) :
    ViewHolder(binding.root) {

    fun bind(item: WeatherListViewModel.WeatherItemState) {
        with(binding) {
            city.text = item.city
            binding.setupDescriptionAndIcon(item)
        }
    }

    private fun ListItemWeatherBinding.setupDescriptionAndIcon(item: WeatherListViewModel.WeatherItemState) {
        weatherIcon.visibility = GONE
        when (item) {
            is WeatherListViewModel.WeatherItemState.Error -> {
                weatherStatus.text = root.context.getString(R.string.failed_to_load_weather)
            }
            is WeatherListViewModel.WeatherItemState.Loaded -> {
                renderLoadedInfo(item)
            }
            is WeatherListViewModel.WeatherItemState.Loading -> {
                weatherStatus.text = root.context.getString(R.string.loading)
            }
            is WeatherListViewModel.WeatherItemState.NotFound -> {
                weatherStatus.text =
                    root.context.getString(R.string.couldnot_find_city_weather_info)
            }
        }
    }

    private fun ListItemWeatherBinding.renderLoadedInfo(item: WeatherListViewModel.WeatherItemState.Loaded) {
        val result = item.result
        // todo render list of weather information instead of fetching first one.
        weatherStatus.text = result.result.weather[0].description
        weatherIcon.visibility = VISIBLE
        weatherIcon.loadImage(result.weatherIconUrl)
        temp.text = root.context.getString(
            R.string.temp_format,
            result.result.main.temp.mapToCelsius()
        )
    }
}