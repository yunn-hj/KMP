package com.dosystem.todo.data.model.weather

import com.dosystem.todo.data.model.geo.CityCoordinate
import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Long,
    val name: String,
    val coord: CityCoordinate,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)
