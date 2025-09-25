package com.dosystem.todo.data.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherSystem(
    val type: Int = 0,
    val id: Int = 0,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
