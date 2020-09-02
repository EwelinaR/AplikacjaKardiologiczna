package com.github.aplikacjakardiologiczna.model.database.dynamodb

import android.app.Activity
import java.text.DateFormat
import java.util.Date


class DatabaseManager constructor(private val activity: Activity) {

    // temporary using fake name
    private val NICK = "ER1234"

    fun markTaskAsDone(taskId: Int) {
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

        val date = dateFormat.format(Date())
        val time = timeFormat.format(Date())

        val task = UpdatePatientProgressAsync(activity, NICK, date, time, taskId)

        task.execute()
    }

    fun createTasks(taskIds: List<Int>, isForToday: Boolean = false) {

    }

    fun getTodaysTasks() {
        DatabaseAccessAsyncTask(activity)
    }

    fun setGroup(group: String) {
        UpdateSingleValueAsync(activity, NICK, "group", group)
    }
}