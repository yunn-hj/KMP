package com.dosystem.todo.data.repository

import com.dosystem.todo.network.Resource
import com.dosystem.todo.network.response.CurWeatherResponse
import com.dosystem.todo.network.response.GeocodingResponse
import com.dosystem.todo.network.response.ThreeHoursForecastResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCoordinates(cityName: String): Flow<Resource<GeocodingResponse>>
    suspend fun getWeather(cityName: String): Flow<Resource<CurWeatherResponse>>
    suspend fun getThreeHoursForecast(lat: Double, long: Double): Flow<Resource<ThreeHoursForecastResponse>>
}