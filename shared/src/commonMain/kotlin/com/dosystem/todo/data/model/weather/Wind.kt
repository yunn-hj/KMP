package com.dosystem.todo.data.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    val speed: Double,
    @SerialName("deg")
    val degree: Int,
    val gust: Double? = null
)
