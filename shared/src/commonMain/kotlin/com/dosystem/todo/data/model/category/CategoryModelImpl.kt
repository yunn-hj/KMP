package com.dosystem.todo.data.model.category

import kotlinx.serialization.Serializable

@Serializable
data class CategoryModelImpl(
    override val id: Long = 0L,
    override val name: String= "일반",
    override val color: String = "gray"
): CategoryModel {

    fun toEntity() = CategoryEntity(
        id = this.id,
        name = this.name,
        color = this.color
    )

    fun copyId(id: Long): CategoryModelImpl = this.copy(id = id)
    fun copyName(name: String): CategoryModelImpl = this.copy(name = name)
    fun copyColor(color: String): CategoryModelImpl = this.copy(color = color)
}

fun getDefaultCategory(): CategoryModel = CategoryModelImpl()