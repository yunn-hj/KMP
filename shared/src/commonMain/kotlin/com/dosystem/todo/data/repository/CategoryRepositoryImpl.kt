package com.dosystem.todo.data.repository

import com.dosystem.todo.data.dao.CategoryDao
import com.dosystem.todo.data.model.category.CategoryEntity

class CategoryRepositoryImpl(
    val categoryDao: CategoryDao
): CategoryRepository {
    override suspend fun insert( category: CategoryEntity) {
        categoryDao.insert(category)
    }

    override suspend fun upsert(category: CategoryEntity) {
        return categoryDao.upsert(category)
    }

    override suspend fun delete(category: CategoryEntity) {
        categoryDao.delete(category)
    }

    override suspend fun deleteById(id: Long) {
        categoryDao.deleteById(id)
    }

    override suspend fun getAll(): List<CategoryEntity> {
        return categoryDao.getAll()
    }

    override suspend fun getById(id: Long): CategoryEntity? {
        return categoryDao.getById(id)
    }

    override suspend  fun getLastId(): Long? {
        return categoryDao.getLastId()
    }
}