package com.dosystem.todo.network.response

import com.dosystem.todo.data.model.weather.City
import com.dosystem.todo.data.model.weather.HourForecast
import kotlinx.serialization.Serializable

@Serializable
data class ThreeHoursForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<HourForecast>,
    val city: City
)
