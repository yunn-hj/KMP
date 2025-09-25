package com.dosystem.todo.data.model.geo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityCoordinate(
    val name: String? = null,
    @SerialName("local_names")
    val localName: LocalName? = null,
    val lat: Double,
    val lon: Double,
    val country: String? = null,
    val state: String? = null
)

@Serializable
data class LocalName(
    val ko: String
)