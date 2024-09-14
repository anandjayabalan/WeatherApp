package com.example.weatherapp

import com.example.weatherapp.api.Remote
import com.example.weatherapp.api.WeatherApiService
import com.example.weatherapp.model.Location
import com.example.weatherapp.model.Main
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.model.Wind
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response


class RemoteTest {

    private val api = mock(WeatherApiService::class.java)
    lateinit var remote: Remote

    @Before
    fun setUp() {
        remote = Remote(api)
    }

    @Test
    fun `test weather for valid city`() {
        runBlocking {
            val response = WeatherResponse(
                "",
                Main(32f, 64, Double.NaN),
                emptyList(),
                Wind(Double.NaN),
                Location("IN")
            )
            `when`(api.getWeather("Chennai")).thenReturn(Response.success(response))
            assertEquals(response, remote.getWeatherReport("Chennai"))
        }
    }

    @Test
    fun `test weather for invalid city`() {
        runBlocking {
            `when`(api.getWeather("")).thenReturn(Response.success(null))
            assertNull(remote.getWeatherReport(""))
        }
    }
}