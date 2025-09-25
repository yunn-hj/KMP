package com.dosystem.todo.data.model.todo

import androidx.room.Embedded
import androidx.room.Relation
import com.dosystem.todo.data.model.category.CategoryEntity
import kotlin.random.Random

data class TodoWithCategory(
    @Embedded
    val todo: TodoEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: CategoryEntity
)


fun getTempTodoWithCategoryList(): List<TodoWithCategory> {
    val list = mutableListOf<TodoWithCategory>()
    val categories = listOf<CategoryEntity>(
        CategoryEntity(0L, "약속", color = "green"),
        CategoryEntity(1L, "목표", color = "red"),
        CategoryEntity(2L, "회사", color = "blue"),
        CategoryEntity(3L, "운동", color = "yellow"),
        CategoryEntity(4L, "연습", color = "black")
    )

    for (i in 0..10) {
        val todo = TodoEntity(
            id = i.toLong(),
            categoryId = i % 5L,
            content = "목표 내용",
            isCompleted = Random.nextBoolean(),
            dueMs = 1756862270000 + i * 100,
            createdMs = 1756862270000,
            updatedMs = 1756862270000
        )
        categories.find { it.id == todo.categoryId }?.let {
            list.add(TodoWithCategory(todo, it))
        }
    }
    return list
}