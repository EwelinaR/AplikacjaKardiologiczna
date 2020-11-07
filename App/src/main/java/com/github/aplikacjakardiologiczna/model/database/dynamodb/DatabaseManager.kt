package com.github.aplikacjakardiologiczna.model.database.dynamodb

import android.content.Context
import android.util.Log
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document
import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.extensions.CalendarExtensions.now
import com.github.aplikacjakardiologiczna.extensions.DateExtensions.polishDateFormat
import com.github.aplikacjakardiologiczna.extensions.DateExtensions.polishTimeFormat
import com.github.aplikacjakardiologiczna.model.database.entity.TaskDetails
import com.github.aplikacjakardiologiczna.model.database.entity.UserInfo
import com.google.gson.Gson
import java.util.Calendar

class DatabaseManager constructor(context: Context) {

    companion object {
        private const val DB_TAG = "DB"
    }

    private val databaseAccess = DatabaseAccess(context)
    private val settings = AppSettings(context)

    private fun getTodaysDate(): String = Calendar.getInstance().now.polishDateFormat

    private fun getTodaysTime(): String = Calendar.getInstance().now.polishTimeFormat

    fun markTaskAsCompleted(taskId: Int) {
        databaseAccess.writeTimeOfTask(taskId, getTodaysDate(), getTodaysTime(), settings.username!!)
        Log.i(DB_TAG, "Completed task: $taskId")
    }

    fun createTasks(userInfo: UserInfo) {
        val jsonUserInfo: String = Gson().toJson(userInfo)
        databaseAccess.addUserInfo(jsonUserInfo)
        Log.i(DB_TAG, jsonUserInfo)
    }

    fun getUserInfo(): UserInfo {
        val doc = databaseAccess.readUserInfo(getTodaysDate(), settings.username!!)
        val user = Gson().fromJson(Document.toJson(doc), UserInfo::class.java)
        Log.i(DB_TAG, user.toString())
        return user
    }

    fun getTasksDetails(ids: List<Int>): List<TaskDetails> {
        val tasks = ArrayList<TaskDetails>()
        for (id in ids) {
            val doc = databaseAccess.readTaskFromDatabase(settings.group!!, id)
            tasks.add(Gson().fromJson(Document.toJson(doc), TaskDetails::class.java))
        }
        Log.i(DB_TAG, tasks.toString())
        return tasks
    }

    fun getTaskIdsFromGroup(): List<Int> {
        val taskIds = databaseAccess.readTasksFromDatabase(settings.group!!)
        return taskIds.items.mapNotNull {
            it["id"]?.n?.toInt()
        }.also {
            Log.i(DB_TAG, taskIds.toString())
        }
    }

    fun getUserGroup(username: String): String {
        val user = databaseAccess.readUserFromDatabase(username)
        return user.items[0]["group"]?.s!!
    }
}
