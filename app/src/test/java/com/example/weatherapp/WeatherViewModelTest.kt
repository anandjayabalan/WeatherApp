package com.example.weatherapp

import com.example.weatherapp.api.WeatherRepository
import com.example.weatherapp.model.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class WeatherViewModelTest {
    val repo: WeatherRepository = mock(WeatherRepository::class.java)
    lateinit var viewmodel: WeatherViewModel

    @Before
    fun setUp() {
        viewmodel = WeatherViewModel(repo)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `test city update`() {
        assertEquals("", viewmodel.city.value)
        viewmodel.city.value = "Chennai"
        assertEquals("Chennai", viewmodel.city.value)
    }

    @Test
    fun `test get weather report success`() {
        runBlocking {
            val response = WeatherResponse(
                "",
                Main(32f, 64, Double.NaN),
                listOf(Weather("","","")),
                Wind(Double.NaN),
                Location("IN")
            )
            Mockito.`when`(repo.getWeatherReport("Chennai")).thenReturn(flow { emit(response) })
            viewmodel.city.value = "Chennai"
            viewmodel.getWeatherReport()
            assertTrue(viewmodel.uiState.value is WeatherResponseState.onSuccess)
        }
    }

    @Test
    fun `test get weather report failure`() {
        runBlocking {
            viewmodel.city.value = ""
            Mockito.`when`(repo.getWeatherReport("Chennai")).thenReturn(flow { emit(null) })
            viewmodel.getWeatherReport()
            assertTrue(viewmodel.uiState.value is WeatherResponseState.onFailed)
        }
    }
}