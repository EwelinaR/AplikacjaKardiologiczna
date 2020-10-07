package com.github.aplikacjakardiologiczna.model.database.dynamodb

import android.content.Context
import android.util.Log
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document
import com.github.aplikacjakardiologiczna.model.database.entity.TaskDetails
import com.github.aplikacjakardiologiczna.model.database.entity.UserInfo
import com.google.gson.Gson
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class DatabaseManager constructor(context: Context) {

    companion object {
        private const val NICK = "TEST"
        private const val DB_TAG = "DB"
    }

    private val databaseAccess = DatabaseAccess(context)

    private fun getTodaysDate(): String {
        val poland = Locale("pl","PL","PL")
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, poland)
        return dateFormat.format(Date())
    }

    private fun getTodaysTime(): String {
        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
        return timeFormat.format(Date())
    }

    fun markTaskAsCompleted(taskId: Int) {
        databaseAccess.writeTimeOfTask(taskId, getTodaysDate(), getTodaysTime(), NICK)
        Log.i(DB_TAG, "Completed task: $taskId")
    }

    fun createTasks(userInfo: UserInfo) {
        val jsonUserInfo: String = Gson().toJson(userInfo)
        databaseAccess.addUserInfo(jsonUserInfo)
        Log.i(DB_TAG, jsonUserInfo)
    }

    fun getUserInfo(): UserInfo {
        val doc = databaseAccess.readUserInfo(getTodaysDate(), NICK)
        val user = Gson().fromJson(Document.toJson(doc), UserInfo::class.java)
        Log.i(DB_TAG, user.toString())
        return user
    }

    fun getTasksDetails(group: String, ids: List<Int>): List<TaskDetails> {
        val tasks = ArrayList<TaskDetails>()
        for (id in ids) {
            val doc = databaseAccess.readTaskFromDatabase(group, id)
            tasks.add(Gson().fromJson(Document.toJson(doc), TaskDetails::class.java))
        }
        Log.i(DB_TAG, tasks.toString())
        return tasks
    }

    fun getTaskIdsFromGroup(group: String): List<Int> {
        val taskIds = databaseAccess.readTasksFromDatabase(group)
        return taskIds.items.mapNotNull {
            it["id"]?.n?.toInt()
        }.also {
            Log.i(DB_TAG, taskIds.toString())
        }
    }
}
