package com.dosystem.todo.di

import com.dosystem.todo.data.repository.CategoryRepository
import com.dosystem.todo.data.repository.CategoryRepositoryImpl
import com.dosystem.todo.data.repository.TodoRepository
import com.dosystem.todo.data.repository.TodoRepositoryImpl
import com.dosystem.todo.data.repository.WeatherRepository
import com.dosystem.todo.data.repository.WeatherRepositoryImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

val repositoryModule = module {
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single<TodoRepository> { TodoRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
}

object Repositories: KoinComponent {
    val weatherRepository: WeatherRepository by inject()
    val todoRepository: TodoRepository by inject()
    val categoryRepository: CategoryRepository by inject()
}