package com.example.weatherapp.dagger

import com.example.weatherapp.api.Remote
import com.example.weatherapp.api.WeatherApiService
import com.example.weatherapp.api.WeatherRepository
import com.example.weatherapp.common.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory

@Module
class WeatherModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemote(api: WeatherApiService): Remote {
        return Remote(api)
    }

    @Provides
    @Singleton
    fun provideRepository(remote: Remote): WeatherRepository {
        return WeatherRepository(remote)
    }
}