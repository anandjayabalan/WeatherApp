package com.example.weatherapp.common

import android.app.Application
import com.example.weatherapp.dagger.DaggerWeatherAppComponent
import com.example.weatherapp.dagger.WeatherAppComponent

class MyApplication : Application() {

    lateinit var appComponent: WeatherAppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerWeatherAppComponent.builder().build()
    }
}