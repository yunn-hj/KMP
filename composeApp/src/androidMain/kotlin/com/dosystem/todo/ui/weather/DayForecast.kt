package com.dosystem.todo.ui.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dosystem.todo.getUnitAnnotatedString
import com.dosystem.todo.viewmodel.DayForecastUiModel

@Composable
fun DayForecastContent(
    modifier: Modifier = Modifier,
    dayForecasts: List<DayForecastUiModel>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .background(Color(0x85858585), RoundedCornerShape(15.dp))
            .padding(20.dp)
    ) {
        for (i in dayForecasts.indices) {
            DayForecastItem(forecast = dayForecasts[i])
        }
    }
}

@Composable
fun DayForecastItem(
    modifier: Modifier = Modifier,
    forecast: DayForecastUiModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = forecast.dayOfWeek,
            color = when (forecast.dayOfWeek) {
                "토" -> Color.Blue
                "일" -> Color.Red
                else -> Color.White
            },
            fontWeight = if (forecast.dayOfWeek == "오늘") FontWeight.Bold else FontWeight.Medium
        )
        Spacer(modifier.weight(1f))
        AsyncImage(
            model = forecast.iconUrl,
            contentDescription = null
        )
        Spacer(modifier.width(20.dp))
        Text(
            text = getUnitAnnotatedString("${forecast.maxTemp}  /  ${forecast.minTemp}", unitSize = 12.sp),
            color = Color.White,
            fontWeight = if (forecast.dayOfWeek == "오늘") FontWeight.Bold else FontWeight.Medium
        )
    }
}