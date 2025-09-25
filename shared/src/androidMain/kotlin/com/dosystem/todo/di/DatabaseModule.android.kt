package com.dosystem.todo.di

import com.dosystem.todo.data.TodoDatabase
import com.dosystem.todo.data.getDatabase
import com.dosystem.todo.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val databaseModule = module {
    single { getDatabase(getDatabaseBuilder(androidContext())) }
    single { get<TodoDatabase>().todoDao() }
    single { get<TodoDatabase>().categoryDao() }
}