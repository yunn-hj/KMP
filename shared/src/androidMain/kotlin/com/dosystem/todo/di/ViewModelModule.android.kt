package com.dosystem.todo.di

import com.dosystem.todo.viewmodel.TodoViewModel
import com.dosystem.todo.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

actual val viewModelModule = module {
    viewModel { WeatherViewModel(get()) }
    viewModel { TodoViewModel(get(), get()) }
}