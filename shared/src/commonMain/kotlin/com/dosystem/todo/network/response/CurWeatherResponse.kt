package com.dosystem.todo.network.response

import com.dosystem.todo.data.model.weather.Clouds
import com.dosystem.todo.data.model.weather.Coordinates
import com.dosystem.todo.data.model.weather.Rain
import com.dosystem.todo.data.model.weather.WeatherDetail
import com.dosystem.todo.data.model.weather.WeatherMain
import com.dosystem.todo.data.model.weather.WeatherSystem
import com.dosystem.todo.data.model.weather.Wind
import kotlinx.serialization.Serializable

@Serializable
data class CurWeatherResponse(
    val coord: Coordinates,
    val weather: List<WeatherDetail>,
    val base: String,
    val main: WeatherMain,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain? = null,
    val clouds: Clouds,
    val dt: Int,
    val sys: WeatherSystem,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)
