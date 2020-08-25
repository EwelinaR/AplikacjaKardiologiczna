package com.github.aplikacjakardiologiczna

import android.content.Context
import android.content.SharedPreferences

class AppSettings(context: Context) {

    private var sharedPreferences: SharedPreferences

    var firstRun: Boolean
        get() = sharedPreferences.getBoolean(PREF_FIRST_RUN, true)
        set(value) = sharedPreferences.edit().putBoolean(PREF_FIRST_RUN, value).apply()

    var username: String?
        get() = sharedPreferences.getString(PREF_USERNAME, null)
        set(value) = sharedPreferences.edit().putString(PREF_USERNAME, value).apply()

    var groupId: Int
        get() = sharedPreferences.getInt(PREF_GROUP_ID, -1)
        set(value) = sharedPreferences.edit().putInt(PREF_GROUP_ID, value).apply()

    companion object {
        const val PREF_FILE_NAME = "PREF_APP_SETTINGS"
        private const val PREF_FIRST_RUN = "PREF_FIRST_RUN"
        private const val PREF_USERNAME = "PREF_USERNAME"
        private const val PREF_GROUP_ID = "PREF_GROUP_ID"
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }
}
