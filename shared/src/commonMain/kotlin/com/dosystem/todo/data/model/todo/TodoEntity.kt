package com.dosystem.todo.data.model.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dosystem.todo.data.model.category.CategoryEntity
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "todo",
    foreignKeys = [ForeignKey(
        CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["category_id"],
        onUpdate = CASCADE,
        onDelete = CASCADE
    )],
    indices = [Index(value = ["category_id"])]
)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo("category_id")
    val categoryId: Long,
    val content: String,
    @ColumnInfo("is_completed")
    val isCompleted: Boolean,
    @ColumnInfo("due_ms")
    val dueMs: Long,
    @ColumnInfo("created_ms")
    val createdMs: Long,
    @ColumnInfo("updated_ms")
    val updatedMs: Long
) {
    fun toModel(): Todo {
        return TodoImpl(
            id = this.id,
            categoryId = this.categoryId,
            content = this.content,
            isCompleted = this.isCompleted,
            dueMs = this.dueMs,
            createdMs = this.createdMs,
            updatedMs = this.updatedMs
        )
    }

    fun copyId(id: Long): TodoEntity = this.copy(id = id)
    fun copyCategoryId(categoryId: Long): TodoEntity = this.copy(categoryId = categoryId)
    fun copyContent(content: String): TodoEntity = this.copy(content = content)
    fun copyCompleted(isCompleted: Boolean): TodoEntity = this.copy(isCompleted = isCompleted)
    fun copyDueMs(dueMs: Long): TodoEntity = this.copy(dueMs = dueMs)
    fun copyUpdatedMs(updatedMs: Long): TodoEntity = this.copy(updatedMs = updatedMs)
}