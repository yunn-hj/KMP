package com.dosystem.todo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dosystem.todo.ui.todo.TodoListScreen
import com.dosystem.todo.ui.weather.WeatherScreen
import com.dosystem.todo.viewmodel.TodoViewModel
import com.dosystem.todo.viewmodel.WeatherViewModel
import org.koin.androidx.compose.koinViewModel

enum class Destination(
    val route: String,
    val label: String
) {
    Weather("weather", "날씨"),
    Todo("todo", "할일")
}

@Composable
fun TodoNavHost(
    navController: NavHostController,
    startDestination: Destination
) {
    val weatherViewModel = koinViewModel<WeatherViewModel>()
    val todoViewModel = koinViewModel<TodoViewModel>()

    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Destination.Weather.route) {
            WeatherScreen(viewModel = weatherViewModel)
        }

        composable(Destination.Todo.route) {
            TodoListScreen()
        }
    }
}