package com.example.weatherapp.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.api.WeatherRepository
import com.example.weatherapp.common.Constants.API_ERROR_MSG
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Weather report view model
 * @author Anand Jayabalan
 */
class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    var city: MutableState<String> = mutableStateOf("")

    val uiState: MutableState<WeatherResponseState> =
        mutableStateOf(WeatherResponseState.Loading(false))

    /**
     * get weather report based on the city
     */
    fun getWeatherReport() {
        showLoader()
        viewModelScope.launch {
            repository.getWeatherReport(city = city.value).catch {
               updateError()
            }.collectLatest { response ->
                response?.also {
                    uiState.value = WeatherResponseState.OnSuccess(it.mapToUIModel())
                } ?: updateError()
            }
        }
    }

    /**
     * show content loader
     */
    private fun showLoader() {
        uiState.value = WeatherResponseState.Loading(true)
    }

    /**
     * Update error in ui model
     */
    private fun updateError() {
        uiState.value = WeatherResponseState.OnFailed(API_ERROR_MSG)
    }

    @Suppress("UNCHECKED_CAST")
    class WeatherViewModelFactory @Inject constructor(private val repo: WeatherRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherViewModel(repo) as T
        }
    }
}