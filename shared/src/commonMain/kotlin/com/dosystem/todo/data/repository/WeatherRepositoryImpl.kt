package com.dosystem.todo.data.repository

import com.dosystem.todo.network.Resource
import com.dosystem.todo.network.WeatherApi
import com.dosystem.todo.network.response.CurWeatherResponse
import com.dosystem.todo.network.response.GeocodingResponse
import com.dosystem.todo.network.response.ThreeHoursForecastResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(
    private val api: WeatherApi
): WeatherRepository {
    override suspend fun getCoordinates(cityName: String): Flow<Resource<GeocodingResponse>> {
        return flow {
            emit(Resource.Loading(true))
            emit(api.getCoordinates(cityName))
        }
    }

    override suspend fun getWeather(cityName: String): Flow<Resource<CurWeatherResponse>> {
        return flow {
            emit(Resource.Loading(true))
            emit(api.getCurWeather(cityName))
        }
    }

    override suspend fun getThreeHoursForecast(lat: Double, long: Double): Flow<Resource<ThreeHoursForecastResponse>> {
        return flow {
            emit(Resource.Loading(true))
            emit(api.getThreeHoursForecast(lat, long))
        }
    }
}