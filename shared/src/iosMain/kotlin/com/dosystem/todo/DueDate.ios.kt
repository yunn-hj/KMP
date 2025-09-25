package com.dosystem.todo

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.dateWithTimeIntervalSince1970

class IOSDueDate(dueMs: Long) : DueDate {
    override val year: Int
    override val month: Int
    override val day: Int
    override val hour: Int
    override val minute: Int
    override val dueDate: String
    override val dueTime: String

    init {
        val sec = dueMs / 1000.0
        val date = NSDate.dateWithTimeIntervalSince1970(sec)

        val calendar = NSCalendar.currentCalendar
        val components = calendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or
                    NSCalendarUnitDay or NSCalendarUnitHour or NSCalendarUnitMinute,
            fromDate = date
        )

        this.year = components.year.toInt()
        this.month = components.month.toInt()
        this.day = components.day.toInt()
        this.hour = components.hour.toInt()
        this.minute = components.minute.toInt()

        val formatter = NSDateFormatter().apply {
            locale = NSLocale("ko_KR")
        }
        formatter.dateFormat = "yyyy년 M월 d일"
        this.dueDate = formatter.stringFromDate(date)

        formatter.dateFormat = "a h시 m분"
        this.dueTime = formatter.stringFromDate(date).replace(" 0분", "")
    }
}

actual fun getDueDateString(dueMs: Long): DueDate = IOSDueDate(dueMs)