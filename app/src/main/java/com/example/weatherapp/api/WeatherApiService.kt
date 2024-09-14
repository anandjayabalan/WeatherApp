package com.example.weatherapp.api

import com.example.weatherapp.common.Constants
import com.example.weatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Weather report api service
 * @author Anand Jayabalan
 */
interface WeatherApiService {
    /**
     * get weather report based on the city
     */
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = Constants.API_KEY,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>

}