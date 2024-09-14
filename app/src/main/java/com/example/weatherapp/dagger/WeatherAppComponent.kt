package com.example.weatherapp.dagger

import com.example.weatherapp.WeatherActivity
import com.example.weatherapp.common.MyApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [WeatherModule::class, ViewModelModule::class])
interface WeatherAppComponent {
    fun inject(application: MyApplication)
    fun inject(activity: WeatherActivity)
}
