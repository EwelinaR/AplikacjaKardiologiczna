package com.github.aplikacjakardiologiczna.model.database.dynamodb

import android.content.Context
import android.util.Log
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document
import com.google.gson.Gson
import java.text.DateFormat
import java.util.Date


class DatabaseManager constructor(context: Context) {

    // temporary using test name
    private val NICK = "TEST"
    private val databaseAccess = DatabaseAccess(context)

    private fun getTodaysDate(): String {
        return "05-09-2020" // for tests
//        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
//        return dateFormat.format(Date())
    }

    private fun getTodaysTime(): String {
        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
        return timeFormat.format(Date())
    }

    fun markTaskAsCompleted(taskId: Int) {
        databaseAccess.writeTimeOfTask(taskId, getTodaysDate(), getTodaysTime(), NICK)
        Log.e("DB", "Completed task: $taskId")
    }

    fun createTasks(taskIds: List<Int>, isForToday: Boolean = false) {
        //TODO
    }

    fun getTasksForToday(): UserInfo {
        val doc = databaseAccess.readUserTasks(getTodaysDate(), NICK)
        val user =  Gson().fromJson(Document.toJson(doc), UserInfo::class.java)
        Log.e("DB", user.toString())
        return user
    }

    fun setGroup(group: String) {
        // TODO
       // UpdateSingleValueAsync(activity, NICK, "group", group)
    }

    fun getTaskDescription(group: String, ids: List<Int>): List<TaskDetails> {
        val tasks = ArrayList<TaskDetails>()
        for(id in ids) {
            val doc = databaseAccess.readTaskFromDatabase(group, id)
            tasks.add(Gson().fromJson(Document.toJson(doc), TaskDetails::class.java))
        }
        Log.e("DB", tasks.toString())
        return tasks
    }

    fun getTaskIdsFromGroup(group: String): List<Int> {
        val taskIds = ArrayList<Int>()
        val result = databaseAccess.readTasksFromDatabase(group)

        for (item in result.items) {
            val s = item.get("id")?.n
            if (s != null) taskIds.add(s.toInt())
        }
        Log.e("DB", taskIds.toString())
        return taskIds
    }
}