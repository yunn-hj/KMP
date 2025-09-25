package com.dosystem.todo.data.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDetail(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)