package com.example.weatherapp.model

sealed class WeatherResponseState {

    data class Loading(val isLoading: Boolean) : WeatherResponseState()

    data class OnSuccess(val uiModel: WeatherUIModel) : WeatherResponseState()

    data class OnFailed(val errorMsg: String) : WeatherResponseState()
}
