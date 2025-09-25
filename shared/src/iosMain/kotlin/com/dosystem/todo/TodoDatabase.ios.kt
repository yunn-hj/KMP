package com.dosystem.todo

import androidx.room.Room
import androidx.room.RoomDatabase
import com.dosystem.todo.data.TodoDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun getDatabaseBuilder(): RoomDatabase.Builder<TodoDatabase> {
    val documentDir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    ).let { requireNotNull(it?.path) }

    return Room.databaseBuilder<TodoDatabase>(
        name = "$documentDir/todo.db"
    )
}