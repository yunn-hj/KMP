package com.dosystem.todo

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class AndroidDueDate(dueMs: Long): DueDate {
    private val epochInstant: Instant = Instant.ofEpochMilli(dueMs)
    private val dateTime = LocalDateTime.ofInstant(epochInstant, ZoneId.systemDefault())
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
    private val timeFormatter = DateTimeFormatter.ofPattern("a h시 m분")

    override val year: Int
        get() = dateTime.year
    override val month: Int
        get() = dateTime.monthValue
    override val day: Int
        get() = dateTime.dayOfMonth
    override val hour: Int
        get() = dateTime.hour
    override val minute: Int
        get() = dateTime.minute
    override val dueDate: String
        get() = dateTime.format(dateFormatter)
    override val dueTime: String
        get() = dateTime.format(timeFormatter).replace(" 0분", "")
}

actual fun getDueDateString(dueMs: Long): DueDate = AndroidDueDate(dueMs)