package com.dosystem.todo.di

import com.dosystem.todo.viewmodel.TodoViewModel
import com.dosystem.todo.viewmodel.WeatherViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module
actual val viewModelModule = module {
    factory { WeatherViewModel(get()) }
    factory { TodoViewModel(get(), get()) }
}

object ViewModels: KoinComponent {
    val weatherViewModel: WeatherViewModel by inject()
    val todoViewModel: TodoViewModel by inject()
}