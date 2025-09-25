package com.dosystem.todo.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            apiModule,
            databaseModule,
            repositoryModule,
            viewModelModule
        )
    }
}