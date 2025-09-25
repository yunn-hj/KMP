package com.dosystem.todo.data.model.todo

import androidx.room.Embedded
import androidx.room.Relation
import com.dosystem.todo.data.model.category.CategoryEntity

data class TodoWithCategory(
    @Embedded
    val todo: TodoEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: CategoryEntity
)