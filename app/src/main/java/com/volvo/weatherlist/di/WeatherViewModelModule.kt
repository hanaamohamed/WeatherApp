package com.volvo.weatherlist.di

import com.volvo.weatherlist.data.WeatherRepository
import com.volvo.weatherlist.data.WeatherRepositoryImpl
import com.volvo.weatherlist.domain.*
import com.volvo.weatherlist.domain.GetCitiesListUseCase
import com.volvo.weatherlist.domain.GetCitiesListUseCaseImpl
import com.volvo.weatherlist.domain.GetCitiesWeatherUseCase
import com.volvo.weatherlist.domain.GetCitiesWeatherUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class WeatherViewModelModule {

    @Binds
    abstract fun bindWeatherRepository(repo: WeatherRepositoryImpl): WeatherRepository

    @Binds
    abstract fun bindGetCitiesWeather(getCitiesWeatherUseCase: GetCitiesWeatherUseCaseImpl): GetCitiesWeatherUseCase

    @Binds
    abstract fun bindGetCitiesUc(getCitiesListUseCase: GetCitiesListUseCaseImpl): GetCitiesListUseCase

    @Binds
    abstract fun bindGetIconUrlUc(getWeatherIconUrlImpl: GetWeatherIconUrlImpl): GetWeatherIconUrl
}