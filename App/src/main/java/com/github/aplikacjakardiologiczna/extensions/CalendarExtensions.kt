package com.github.aplikacjakardiologiczna.extensions

import java.util.Calendar
import java.util.Date


object CalendarExtensions {

    val Calendar.startOfTomorrow: Date
        get() {
            add(Calendar.DAY_OF_YEAR, 1)
            return atStartOfDay(time)
        }

    val Calendar.startOfToday: Date
        get() = atStartOfDay(time)

    val Calendar.now: Date
        get() = time

    private fun Calendar.atStartOfDay(date: Date): Date =
        this.apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
}
