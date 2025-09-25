package com.dosystem.todo.ui.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dosystem.todo.getUnitAnnotatedString
import com.dosystem.todo.viewmodel.HourForecastUiModel

@Composable
fun HourForecastContent(
    modifier: Modifier = Modifier,
    hourForecasts: List<HourForecastUiModel>
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(20.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .background(Color(0x85858585), RoundedCornerShape(15.dp))
    ) {
        items(hourForecasts.size) {
            HourForecastItem(
                forecast = hourForecasts[it]
            )
        }
    }
}

@Composable
fun HourForecastItem(
    modifier: Modifier = Modifier,
    forecast: HourForecastUiModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(Color.Transparent)
    ) {
        Text(
            text = forecast.time,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier.height(10.dp))
        AsyncImage(
            model = forecast.iconUrl,
            contentDescription = null
        )
        Spacer(modifier.height(6.dp))
        Text(
            text = getUnitAnnotatedString(forecast.temp, 12.sp),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}