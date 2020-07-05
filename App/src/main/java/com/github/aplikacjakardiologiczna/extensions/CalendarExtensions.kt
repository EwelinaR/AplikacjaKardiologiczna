package com.github.aplikacjakardiologiczna.extensions

import java.util.Calendar
import java.util.Date


object CalendarExtensions {

    val Calendar.tomorrow: Date
        get() = atStartOfDay(time.apply { add(Calendar.DAY_OF_MONTH, 1) })

    val Calendar.today: Date
        get() = atStartOfDay(time)

    private fun Calendar.atStartOfDay(date: Date): Date =
        this.apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
}
