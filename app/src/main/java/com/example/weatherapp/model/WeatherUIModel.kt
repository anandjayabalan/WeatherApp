package com.example.weatherapp.model

/**
 * Weather UI model
 * @param title Short description
 * @param temp temperature
 * @param description temp description
 * @param icon icon
 * @param windSpeed wind speed
 * @param humidity humidity level
 * @param pressure Pressure of atoms
 * @param location location details
 *
 * @author Anand Jayabalan
 */
data class WeatherUIModel(
    val title: String,
    val temp: String,
    val description: String,
    val icon: String,
    val windSpeed: String,
    val humidity: String,
    val pressure: String,
    val location: String
)