package com.dosystem.todo.ui.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dosystem.todo.data.model.geo.CityCoordinate
import com.dosystem.todo.getUnitAnnotatedString
import com.dosystem.todo.viewmodel.OtherInfoUiModel
import com.dosystem.todo.viewmodel.WeatherState
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherInfo(
    modifier: Modifier = Modifier,
    state: WeatherState
) {
    val temp = "${state.curWeather!!.main.temp.roundToInt()}℃"
    val tempMax = "${state.curWeather!!.main.tempMax.roundToInt()}℃"
    val tempMin = "${state.curWeather!!.main.tempMin.roundToInt()}℃"

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = state.coordinates?.firstOrNull()?.localName?.ko
                ?: state.curWeather!!.name,
            color = Color.White,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
        Text(
            text = temp,
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
        Text(
            text = state.curWeather!!.weather.first().description,
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
        Text(
            text = "최고 $tempMax\n최저 $tempMin",
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
fun OtherWeatherInfo(
    modifier: Modifier = Modifier,
    otherItems: List<OtherInfoUiModel>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        for (i in 0 until otherItems.size / 2) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                WeatherItem(
                    parentModifier = modifier.weight(1f),
                    title = otherItems[i * 2].title,
                    content = otherItems[i * 2].content
                )
                WeatherItem(
                    parentModifier = modifier.weight(1f),
                    title = otherItems[i * 2 + 1].title,
                    content = otherItems[i * 2 + 1].content
                )
            }
        }
    }

}

@Composable
fun CitySearchDropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    availableCities: List<CityCoordinate>,
    onDismiss: () -> Unit,
    onCityClick: (Int) -> Unit
) {
    Box {
        DropdownMenu(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White),
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            for (i in availableCities.indices) {
                DropdownMenuItem(
                    modifier = modifier,
                    text = {
                        Text(text = availableCities[i].localName?.ko ?: "")
                    },
                    onClick = { onCityClick(i) }
                )
            }
        }
    }
}

@Composable
fun WeatherItem(
    modifier: Modifier = Modifier,
    parentModifier: Modifier,
    title: String,
    content: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = parentModifier
            .background(Color(0x85858585), RoundedCornerShape(15.dp))
            .padding(20.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.White
        )
        Spacer(modifier.height(10.dp))

        Text(
            text = getUnitAnnotatedString(
                prev = content,
                unitSize = 16.sp
            ),
            fontSize = 20.sp,
            color = Color.White
        )
    }
}