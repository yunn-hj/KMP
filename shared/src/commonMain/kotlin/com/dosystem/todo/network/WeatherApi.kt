package com.dosystem.todo.network

import com.dosystem.todo.network.response.CurWeatherResponse
import com.dosystem.todo.network.response.GeocodingResponse
import com.dosystem.todo.network.response.ThreeHoursForecastResponse

interface WeatherApi {
    suspend fun getCoordinates(cityName: String): Resource<GeocodingResponse>
    suspend fun getThreeHoursForecast(lat: Double, long: Double): Resource<ThreeHoursForecastResponse>
    suspend fun getCurWeather(cityName: String): Resource<CurWeatherResponse>
}