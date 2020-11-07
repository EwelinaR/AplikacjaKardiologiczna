package com.github.aplikacjakardiologiczna

import android.content.Context
import android.content.SharedPreferences

class AppSettings(context: Context) {

    private var sharedPreferences: SharedPreferences

    var username: String?
        get() = sharedPreferences.getString(PREF_USERNAME, null)
        set(value) = sharedPreferences.edit().putString(PREF_USERNAME, value).apply()

    var group: String?
        get() = sharedPreferences.getString(PREF_GROUP, null)
        set(value) = sharedPreferences.edit().putString(PREF_GROUP, value).apply()

    companion object {
        const val PREF_FILE_NAME = "PREF_APP_SETTINGS"
        private const val PREF_USERNAME = "PREF_USERNAME"
        private const val PREF_GROUP = "PREF_GROUP"
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }
}
