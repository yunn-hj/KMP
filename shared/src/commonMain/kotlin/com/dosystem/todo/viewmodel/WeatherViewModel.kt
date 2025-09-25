package com.dosystem.todo.viewmodel

import androidx.lifecycle.ViewModel
import com.dosystem.todo.data.model.geo.CityCoordinate
import com.dosystem.todo.data.model.weather.HourForecast
import com.dosystem.todo.data.repository.WeatherRepository
import com.dosystem.todo.network.Resource
import com.dosystem.todo.network.response.CurWeatherResponse
import com.dosystem.todo.network.response.GeocodingResponse
import com.dosystem.todo.network.response.ThreeHoursForecastResponse
import com.dosystem.todo.toKoreanDayOfWeek
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.Instant

data class HourForecastUiModel(
    val time: String,
    val iconUrl: String,
    val temp: String
)

data class DayForecastUiModel(
    val dayOfWeek: String,
    val iconUrl: String,
    val minTemp: String,
    val maxTemp: String
)

data class OtherInfoUiModel(
    val title: String,
    val content: String
)

class WeatherViewModel(
    private val weatherRepo: WeatherRepository,
): ContainerHost<WeatherState, WeatherSideEffect>, ViewModel() {

    override val container: Container<WeatherState, WeatherSideEffect> = container(WeatherState())
    @NativeCoroutines
    val state: StateFlow<WeatherState> = container.stateFlow

    fun onSearchQueryChanged(query: String) = intent {
        reduce { state.copy(searchQuery = query) }
    }

    fun onDropdownExpandedChanged(expanded: Boolean) = intent {
        reduce { state.copy(isDropdownExpanded = expanded) }
    }

    fun getCoordinates() = intent {
        reduce { state.copy(isLoading = true, isDropdownExpanded = true) }
        weatherRepo.getCoordinates(state.searchQuery).collect {
            when (it) {
                is Resource.Success ->
                    reduce { state.copy(coordinates = it.data, isLoading = false, error = "") }
                is Resource.Loading ->
                    reduce { state.copy(isLoading = true) }
                is Resource.Error ->
                    reduce { state.copy(error = it.error ?: "Something went wrong") }
            }
        }
    }

    fun getCurWeather(coord: CityCoordinate) = intent {
        reduce { state.copy(isLoading = true, isDropdownExpanded = false) }

        val weather = weatherRepo.getWeather(coord.name ?: "")
        val forecast = weatherRepo.getThreeHoursForecast(coord.lat, coord.lon)

        weather.combine(forecast) { weather, forecast ->
            weather to forecast
        }.collect { (weather, forecast) ->
            when {
                weather is Resource.Success && forecast is Resource.Success ->
                    reduce {
                        state.copy(
                            curWeather = weather.data,
                            threeHoursForecast = forecast.data,
                            isLoading = false,
                            error = "",
                            hourForecasts = getHourForecastUiModel(forecast.data?.list),
                            dayForecasts = getDayForecastUiModel(forecast.data?.list),
                            otherInfo = getOtherInfoUiModel(weather.data)
                        )
                    }
                weather is Resource.Loading || forecast is Resource.Loading ->
                    reduce { state.copy(isLoading = true) }
                weather is Resource.Error || forecast is Resource.Error ->
                    reduce { state.copy(error = "Something went wrong") }
            }
        }
    }

    private fun getHourForecastUiModel(list: List<HourForecast>?): List<HourForecastUiModel> {
        return list?.take(8)?.map {
            val instant = Instant.fromEpochSeconds(it.dt)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

            HourForecastUiModel(
                time = "${localDateTime.hour}시",
                iconUrl = "https://openweathermap.org/img/wn/${it.weather.first().icon}@2x.png",
                temp = "${it.main.temp.roundToInt()}℃"
            )
        } ?: emptyList()
    }

    private fun getDayForecastUiModel(list: List<HourForecast>?): List<DayForecastUiModel> {
        return list?.groupBy {
            val instant = Instant.fromEpochSeconds(it.dt)
            instant.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek
        }?.map { entry ->
            val firstItem = entry.value.first()
            val minTemp = entry.value.minOf { it.main.temp }.roundToInt()
            val maxTemp = entry.value.maxOf { it.main.temp }.roundToInt()
            val isToday = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek == entry.key

            DayForecastUiModel(
                dayOfWeek = if (isToday) "오늘" else entry.key.toKoreanDayOfWeek(),
                iconUrl = "https://openweathermap.org/img/wn/${firstItem.weather.first().icon}@2x.png",
                minTemp = "${minTemp}℃",
                maxTemp = "${maxTemp}℃"
            )
        } ?: emptyList()
    }

    private fun getOtherInfoUiModel(weather: CurWeatherResponse?): List<OtherInfoUiModel> {
        return if (weather == null) {
            listOf()
        } else {
            listOf(
                OtherInfoUiModel("체감온도", "${weather.main.feelsLike.roundToInt()}℃"),
                OtherInfoUiModel("강수량", "${(weather.rain?.oneHour ?: 0.0).roundToInt()}mm"),
                OtherInfoUiModel("습도", "${weather.main.humidity}%"),
                OtherInfoUiModel("풍속", "${weather.wind.speed}m/s"),
                OtherInfoUiModel("기압", "${weather.main.pressure}hPa"),
                OtherInfoUiModel("가시거리", "${weather.visibility / 1000}km")
            )
        }
    }
}

data class WeatherState(
    val coordinates: GeocodingResponse? = null,
    val curWeather: CurWeatherResponse? = null,
    val threeHoursForecast: ThreeHoursForecastResponse? = null,
    val isLoading: Boolean = false,
    val error: String = "",
    val hourForecasts: List<HourForecastUiModel> = emptyList(),
    val dayForecasts: List<DayForecastUiModel> = emptyList(),
    val otherInfo: List<OtherInfoUiModel> = emptyList(),
    val searchQuery: String = "",
    val isDropdownExpanded: Boolean = false
)

sealed interface WeatherSideEffect {}