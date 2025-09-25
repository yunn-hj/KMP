package com.dosystem.todo.data.repository

import com.dosystem.todo.data.model.category.CategoryEntity

interface CategoryRepository {
    suspend fun insert(category: CategoryEntity)
    suspend fun upsert(category: CategoryEntity)
    suspend fun delete(category: CategoryEntity)
    suspend fun deleteById(id: Long)
    suspend fun getAll(): List<CategoryEntity>
    suspend fun getById(id: Long): CategoryEntity?
    suspend fun getLastId(): Long?
}