package com.volvo.weatherlist.di

import com.volvo.weatherlist.WeatherRepository
import com.volvo.weatherlist.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class WeatherViewModelModule {

    @Binds
    abstract fun bindWeatherRepository(repo: WeatherRepositoryImpl): WeatherRepository
}