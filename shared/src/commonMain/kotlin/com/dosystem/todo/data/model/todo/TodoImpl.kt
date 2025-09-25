package com.dosystem.todo.data.model.todo

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class TodoImpl @OptIn(ExperimentalTime::class) constructor(
    override val id: Long = 0L,
    override val categoryId: Long = 0L,
    override val content: String = "",
    override val isCompleted: Boolean = false,
    override val dueMs: Long = 0L,
    override val updatedMs: Long = Clock.System.now().toEpochMilliseconds(),
    override val createdMs: Long = Clock.System.now().toEpochMilliseconds()
): Todo {
    fun toEntity(): TodoEntity {
        return TodoEntity(
            id = this.id,
            categoryId = this.categoryId,
            content = this.content,
            isCompleted = this.isCompleted,
            dueMs = this.dueMs,
            createdMs = this.createdMs,
            updatedMs = this.updatedMs
        )
    }

    fun copyId(id: Long): TodoImpl = this.copy(id = id)
    fun copyCategoryId(categoryId: Long): TodoImpl = this.copy(categoryId = categoryId)
    fun copyContent(content: String): TodoImpl = this.copy(content = content)
    fun copyCompleted(isCompleted: Boolean): TodoImpl = this.copy(isCompleted = isCompleted)
    fun copyDueMs(dueMs: Long): TodoImpl = this.copy(dueMs = dueMs)
    fun copyUpdatedMs(updatedMs: Long): TodoImpl = this.copy(updatedMs = updatedMs)
}

fun getDefaultTodo(): Todo = TodoImpl()