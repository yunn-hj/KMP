package com.dosystem.todo.data.model.todo

interface Todo {
    val id: Long
    val categoryId: Long
    val content: String
    val isCompleted: Boolean
    val dueMs: Long
    val createdMs: Long
    val updatedMs: Long
}