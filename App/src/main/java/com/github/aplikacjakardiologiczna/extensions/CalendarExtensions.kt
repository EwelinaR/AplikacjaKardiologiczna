package com.github.aplikacjakardiologiczna.extensions

import java.util.Calendar
import java.util.Date


object CalendarExtensions {
    fun Calendar.atStartOfDay(date: Date): Date =
        this.apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time


    fun Calendar.atEndOfDay(date: Date): Date =
        this.apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.time

    val Calendar.tomorrow: Date
        get() = time.apply { add(Calendar.DAY_OF_MONTH, 1) }
}
