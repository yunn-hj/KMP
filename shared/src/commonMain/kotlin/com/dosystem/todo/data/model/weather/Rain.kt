package com.dosystem.todo.data.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rain(
    @SerialName("1h")
    val oneHour: Double = 0.0,
    @SerialName("3h")
    val threeHours: Double = 0.0
)
