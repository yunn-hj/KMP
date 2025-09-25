package com.dosystem.todo.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.dosystem.todo.data.dao.CategoryDao
import com.dosystem.todo.data.dao.TodoDao
import com.dosystem.todo.data.model.category.CategoryEntity
import com.dosystem.todo.data.model.todo.TodoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [TodoEntity::class, CategoryEntity::class], version = 2)
@ConstructedBy(TodoDatabaseConstructor::class)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun categoryDao(): CategoryDao
}

@Suppress("KotlinNoActualForExpect")
expect object TodoDatabaseConstructor: RoomDatabaseConstructor<TodoDatabase> {
    override fun initialize(): TodoDatabase
}

fun getDatabase(builder: RoomDatabase.Builder<TodoDatabase>): TodoDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}