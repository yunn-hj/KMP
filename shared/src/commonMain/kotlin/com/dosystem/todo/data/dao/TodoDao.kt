package com.dosystem.todo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.dosystem.todo.data.model.todo.TodoEntity
import com.dosystem.todo.data.model.todo.TodoWithCategory

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(todo: TodoEntity)

    @Upsert
    suspend fun upsert(todo: TodoEntity)

    @Delete
    suspend fun delete(todo: TodoEntity)

    @Query("delete from todo where id = (:id)")
    suspend fun deleteById(id: Long)

    @Transaction
    @Query("select * from todo")
    suspend fun getAll(): List<TodoWithCategory>

    @Transaction
    @Query("select * from todo where id = (:id)")
    suspend fun getById(id: Long): TodoWithCategory

    @Transaction
    @Query("select * from todo where category_id in (:categoryId)")
    suspend fun getByCategoryIds(vararg categoryId: Long): List<TodoWithCategory>

    @Transaction
    @Query("select * from todo where content like '%' || (:keyword) || '%'")
    suspend fun getByKeyword(keyword: String): List<TodoWithCategory>
}