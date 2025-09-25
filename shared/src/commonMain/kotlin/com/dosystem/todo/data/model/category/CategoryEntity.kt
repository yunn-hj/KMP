package com.dosystem.todo.data.model.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val color: String
) {
    fun toModel() = CategoryModelImpl(
        id = this.id,
        name = this.name,
        color = this.color
    ) as CategoryModel

    fun copyId(id: Long): CategoryEntity = this.copy(id = id)
    fun copyName(name: String): CategoryEntity = this.copy(name = name)
    fun copyColor(color: String): CategoryEntity = this.copy(color = color)
}