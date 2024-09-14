package com.example.weatherapp

import com.example.weatherapp.api.Remote
import com.example.weatherapp.api.WeatherRepository
import com.example.weatherapp.model.Location
import com.example.weatherapp.model.Main
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.model.Wind
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class RepositoryTest {

    val remote: Remote = mock(Remote::class.java)
    lateinit var repo: WeatherRepository

    @Before
    fun setUp() {
        repo = WeatherRepository(remote)
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
            `when`(remote.getWeatherReport("Chennai")).thenReturn(response)
            repo.getWeatherReport("Chennai").collect{
                assertEquals(response,it)
            }
        }
    }

    @Test
    fun `test weather for invalid city`() {
        runBlocking {
            `when`(remote.getWeatherReport("")).thenReturn(null)
            repo.getWeatherReport("").collect{
                assertNull(it)
            }
        }
    }
}