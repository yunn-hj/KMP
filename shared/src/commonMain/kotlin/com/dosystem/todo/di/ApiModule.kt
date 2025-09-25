package com.dosystem.todo.di

import com.dosystem.todo.network.WeatherApi
import com.dosystem.todo.network.WeatherApiImpl
import org.koin.dsl.module

val apiModule = module {
    single<WeatherApi> { WeatherApiImpl() }
}