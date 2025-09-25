package com.dosystem.todo.ui.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dosystem.todo.data.model.weather.HourForecast
import com.dosystem.todo.getForecastAnimation
import com.dosystem.todo.viewmodel.WeatherState
import com.dosystem.todo.viewmodel.WeatherViewModel
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors = listOf(Color(0xFF2B7CCC), Color(0xFF7CCCFF))))
    ) {
        WeatherScreenBackground(
            forecast = state.threeHoursForecast?.list?.find {
                it.dt * 1000 > System.currentTimeMillis()
            }
        )
        WeatherScreenForeground(
            state = state,
            viewModel = viewModel
        )
    }
}

@Composable
fun WeatherScreenForeground(
    modifier: Modifier = Modifier,
    state: WeatherState,
    viewModel: WeatherViewModel,
) {
    LazyColumn {
        item {
            TextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions {
                    viewModel.getCoordinates()
                },
                placeholder = {
                    Text(
                        text = "도시 이름을 검색하세요",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                },
                shape = RectangleShape,
                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = modifier.fillMaxWidth()
            )

            if (!state.coordinates.isNullOrEmpty()) {
                CitySearchDropdown(
                    expanded = state.isDropdownExpanded,
                    availableCities = state.coordinates!!,
                    onDismiss = { viewModel.onDropdownExpandedChanged(false) },
                    onCityClick = {
                        viewModel.getCurWeather(state.coordinates!![it])
                    }
                )
            }
        }

        item {
            if (state.curWeather == null || state.threeHoursForecast == null || state.error.isNotEmpty()) {
                Text(
                    text = "검색 결과가 없습니다.",
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 56.dp)
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    CurrentWeatherInfo(state = state)
                    HourForecastContent(hourForecasts = state.hourForecasts)
                    DayForecastContent(dayForecasts = state.dayForecasts)
                    OtherWeatherInfo(otherItems = state.otherInfo)
                }
                Spacer(modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun WeatherScreenBackground(
    modifier: Modifier = Modifier,
    forecast: HourForecast?
) {
    val anim = getForecastAnimation(forecast)
    if (forecast == null || anim == null) return

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(anim)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier
                .padding(20.dp)
                .size(100.dp)
                .align(Alignment.TopEnd)
        )
    }
}