package com.example.weatherapp.api

import javax.inject.Inject

/**
 * App Remote
 * @author Anand Jayabalan
 */
class Remote @Inject constructor(val api: WeatherApiService) {
    /**
     * get weather report
     */
    suspend fun getWeatherReport(city: String) = api.getWeather(city).body()
}