package com.dosystem.todo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.dosystem.todo.data.model.category.CategoryEntity

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: CategoryEntity)

    @Upsert
    suspend fun upsert(category: CategoryEntity)

    @Delete
    suspend fun delete(category: CategoryEntity)

    @Query("delete from category where id = (:id)")
    suspend fun deleteById(id: Long)

    @Query("select * from category")
    suspend fun getAll(): List<CategoryEntity>

    @Query("select * from category where id = (:id)")
    suspend fun getById(id: Long): CategoryEntity?

    @Query("select id from category order by id desc limit 1")
    suspend fun getLastId(): Long?
}