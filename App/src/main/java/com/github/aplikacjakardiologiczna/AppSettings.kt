package com.github.aplikacjakardiologiczna

import android.content.Context
import android.content.SharedPreferences

class AppSettings(context: Context) {

    private var sharedPreferences: SharedPreferences

    var firstRun: Boolean
        get() = sharedPreferences.getBoolean(PREF_FIRST_RUN, true)
        set(value) = sharedPreferences.edit().putBoolean(PREF_FIRST_RUN, value).apply()

    companion object {
        const val PREF_FILE_NAME = "PREF_APP_SETTINGS"
        private const val PREF_FIRST_RUN = "PREF_FIRST_RUN"
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }
}
