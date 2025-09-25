package com.dosystem.todo.data.repository

import com.dosystem.todo.data.model.todo.TodoEntity
import com.dosystem.todo.data.model.todo.TodoWithCategory
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun insert(todo: TodoEntity)
    suspend fun upsert(todo: TodoEntity)
    suspend fun delete(todo: TodoEntity)
    suspend fun deleteById(id: Long)
    suspend fun getAll(): List<TodoWithCategory>
    suspend fun getById(id: Long): TodoWithCategory
    suspend fun getByCategoryIds(vararg categoryId: Long): List<TodoWithCategory>
    suspend fun getByKeyword(keyword: String): List<TodoWithCategory>
}