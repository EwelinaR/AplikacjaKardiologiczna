package com.github.aplikacjakardiologiczna.extensions

import java.text.DateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateExtensions {

    private val polishLocale = Locale("pl", "PL", "PL")

    val Date.polishDateFormat: String
        get() {
            val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, polishLocale)
            return dateFormat.format(this)
        }

    val Date.polishTimeFormat: String
        get() {
            val timeZone = TimeZone.getTimeZone("Europe/Warsaw")
            val dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, polishLocale)
            dateFormat.timeZone = timeZone
            return dateFormat.format(this)
        }
}
