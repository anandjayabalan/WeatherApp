package com.example.weatherapp.dagger

import com.example.weatherapp.api.WeatherRepository
import com.example.weatherapp.model.WeatherViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideWeatherViewModelFactory(
        repo: WeatherRepository
    ): WeatherViewModel.WeatherViewModelFactory {
        return WeatherViewModel.WeatherViewModelFactory(repo)
    }
}