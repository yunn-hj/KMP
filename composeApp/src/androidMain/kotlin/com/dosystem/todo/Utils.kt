package com.dosystem.todo

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.core.app.NotificationCompat
import com.dosystem.todo.data.model.todo.TodoWithCategory
import com.dosystem.todo.data.model.weather.HourForecast
import java.util.Calendar

private var notifiedTodo = false

fun notifyTodo(context: Context, todoList: List<TodoWithCategory>) {
    if (notifiedTodo || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val isGranted = context.checkSelfPermission(
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED
    Log.e("yhj_utils", "$isGranted")
    if (!isGranted) return

    val finished = todoList
        .filter { !it.todo.isCompleted && it.todo.dueMs <= System.currentTimeMillis() }
        .ifEmpty { return }
        .joinToString { it.todo.content.ifEmpty { "이름없음" } }
    Log.e("yhj_utils", "$finished")
    val notiManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notiChannel = NotificationChannel(
        "notify_todo_channel_id", "notify_todo", NotificationManager.IMPORTANCE_HIGH
    )
    val notiBuilder = NotificationCompat.Builder(context, "notify_todo_channel_id").apply {
        setContentTitle("미완료 할일 알림")
        setContentText(finished)
        setSmallIcon(R.mipmap.ic_launcher_round)
    }

    notiManager.createNotificationChannel(notiChannel)
    notiManager.notify(1, notiBuilder.build())
    notifiedTodo = true
}

fun getColor(colorName: String): Color {
    return when (colorName) {
        "red" -> Color.Red
        "yellow" -> Color.Yellow
        "green" -> Color.Green
        "blue" -> Color.Blue
        "gray" -> Color.Gray
        else -> Color.Gray
    }
}

fun getUnitAnnotatedString(prev: String, unitSize: TextUnit): AnnotatedString {
    return buildAnnotatedString {
        val regex = "([0-9.]+|[^0-9.]+)".toRegex()
        regex.findAll(prev).forEach { matchResult ->
            val part = matchResult.value

            if (part.all { it.isDigit() || it == '.' }) {
                append(part)
            } else {
                withStyle(style = SpanStyle(fontSize = unitSize, fontWeight = FontWeight.Light)) {
                    append(part)
                }
            }
        }
    }
}

fun getForecastAnimation(forecast: HourForecast?): Int? {
    if (forecast == null) return null

    val cal = Calendar.getInstance()
    val sunset = Calendar.getInstance().apply {
        timeInMillis = forecast.dt * 1000
    }
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    val isMorning = hour <= sunset.get(Calendar.HOUR_OF_DAY)
    val forecast = forecast.weather.first().main.lowercase()

    return when {
        forecast.contains("sun") -> {
            if (isMorning) R.raw.anim_sunny_morning else R.raw.anim_sunny_night
        }

        forecast.contains("cloud") -> {
            R.raw.anim_cloudy
        }

        forecast.contains("rain") -> {
            if (isMorning) R.raw.anim_rainy_morning else R.raw.anim_rainy_night
        }

        forecast.contains("snow") -> {
            R.raw.anim_snowy
        }

        forecast.contains("thunder") -> {
            R.raw.anim_thunder
        }

        else -> null
    }
}