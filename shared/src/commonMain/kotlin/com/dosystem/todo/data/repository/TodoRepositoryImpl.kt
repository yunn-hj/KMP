package com.dosystem.todo.data.repository

import com.dosystem.todo.data.dao.TodoDao
import com.dosystem.todo.data.model.todo.TodoEntity
import com.dosystem.todo.data.model.todo.TodoWithCategory

class TodoRepositoryImpl(
    val todoDao: TodoDao
): TodoRepository {
    override suspend fun insert(todo: TodoEntity) {
        todoDao.insert(todo)
    }

    override suspend fun upsert(todo: TodoEntity) {
        todoDao.upsert(todo)
    }

    override suspend fun delete(todo: TodoEntity) {
        todoDao.delete(todo)
    }

    override suspend fun deleteById(id: Long) {
        todoDao.deleteById(id)
    }

    override suspend fun getAll(): List<TodoWithCategory> =
        todoDao.getAll()

    override suspend fun getById(id: Long): TodoWithCategory =
        todoDao.getById(id)

    override suspend fun getByCategoryIds(vararg categoryId: Long): List<TodoWithCategory> =
        todoDao.getByCategoryIds(*categoryId)

    override suspend fun getByKeyword(keyword: String): List<TodoWithCategory> =
        todoDao.getByKeyword(keyword.lowercase())

}