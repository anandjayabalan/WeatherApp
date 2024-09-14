package com.example.weatherapp.api

import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Weather report repository
 * @author Anand Jayabalan
 */
class WeatherRepository @Inject constructor(private val remote: Remote) {

    /**
     * get weather report
     */
    fun getWeatherReport(city: String): Flow<WeatherResponse?> {
        return flow {
           emit(remote.getWeatherReport(city))
        }
    }
}