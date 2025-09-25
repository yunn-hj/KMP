package com.dosystem.todo.network

import com.dosystem.todo.network.response.CurWeatherResponse
import com.dosystem.todo.network.response.GeocodingResponse
import com.dosystem.todo.network.response.ThreeHoursForecastResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url

class WeatherApiImpl(): WeatherApi {
    companion object {
        private const val API_KEY = "e2e05c4d332f3b85620073d68bfa2657"
    }

    override suspend fun getCoordinates(cityName: String): Resource<GeocodingResponse> {
        try {
            val response = client.get {
                url("geo/1.0/direct?q=$cityName&appid=$API_KEY")
            }
            return Resource.Success(response.body())
        } catch (e: RedirectResponseException) {
            // handle 3xx codes
            return (Resource.Error(e.response.status.description))

        } catch (e: ClientRequestException) {
            //handle 4xx error codes
            return (Resource.Error(e.response.status.description))

        } catch (e: ServerResponseException) {
            //handle 5xx error codes
            return (Resource.Error(e.response.status.description))
        } catch (e: Exception) {
            return (Resource.Error(e.message ?: "Something went wrong"))
        }
    }
    override suspend fun getCurWeather(cityName: String): Resource<CurWeatherResponse> {
        try {
            val response = client.get {
                url("data/2.5/weather?q=$cityName&appid=$API_KEY&units=metric&lang=kr")
            }
            return Resource.Success(response.body())
        } catch (e: RedirectResponseException) {
            // handle 3xx codes
            return (Resource.Error(e.response.status.description))

        } catch (e: ClientRequestException) {
            //handle 4xx error codes
            return (Resource.Error(e.response.status.description))

        } catch (e: ServerResponseException) {
            //handle 5xx error codes
            return (Resource.Error(e.response.status.description))
        } catch (e: Exception) {
            return (Resource.Error(e.message ?: "Something went wrong"))
        }
    }

    override suspend fun getThreeHoursForecast(
        lat: Double,
        long: Double
    ): Resource<ThreeHoursForecastResponse> {
        try {
            val response = client.get {
                url("data/2.5/forecast?lat=$lat&lon=$long&appid=$API_KEY&units=metric&lang=kr")
            }
            return Resource.Success(response.body())
        } catch (e: RedirectResponseException) {
            // handle 3xx codes
            return (Resource.Error(e.response.status.description))

        } catch (e: ClientRequestException) {
            //handle 4xx error codes
            return (Resource.Error(e.response.status.description))

        } catch (e: ServerResponseException) {
            //handle 5xx error codes
            return (Resource.Error(e.response.status.description))
        } catch (e: Exception) {
            return (Resource.Error(e.message ?: "Something went wrong"))
        }
    }
}