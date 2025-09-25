package com.dosystem.todo.data.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourForecast(
    val dt: Long,
    val main: WeatherMain,
    val weather: List<WeatherDetail>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val rain: Rain? = null,
    @SerialName("dt_txt")
    val dtText: String
)