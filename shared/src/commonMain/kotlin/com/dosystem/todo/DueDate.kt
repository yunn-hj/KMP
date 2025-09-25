package com.dosystem.todo

interface DueDate {
    val year: Int
    val month: Int
    val day: Int
    val hour: Int
    val minute: Int
    val dueDate: String
    val dueTime: String
}

expect fun getDueDateString(dueMs: Long): DueDate
