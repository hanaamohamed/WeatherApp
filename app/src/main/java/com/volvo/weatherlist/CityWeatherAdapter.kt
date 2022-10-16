package com.volvo.weatherlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
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
            binding.setupDescription(item)
        }
    }

    private fun ListItemWeatherBinding.setupDescription(item: WeatherListViewModel.WeatherItemState) {
        when (item) {
            is WeatherListViewModel.WeatherItemState.Error -> {
                weatherStatus.text = root.context.getString(R.string.failed_to_load_weather)
            }
            is WeatherListViewModel.WeatherItemState.Loaded -> {
                weatherStatus.text = item.weather.description
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
}