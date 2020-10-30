package com.github.aplikacjakardiologiczna.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.entity.TaskDetails
import com.github.aplikacjakardiologiczna.model.database.entity.UserInfo
import com.github.aplikacjakardiologiczna.model.database.repository.TaskDetailsRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AlarmNotificationReceiver : BroadcastReceiver(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + Job()

    private lateinit var dynamoDb: DatabaseManager
    private lateinit var userTaskRepository: UserTaskRepository
    private lateinit var taskDetailsRepository: TaskDetailsRepository
    private lateinit var context: Context
    private lateinit var intent: Intent

    override fun onReceive(context: Context, intent: Intent) {
        this.context = context
        this.intent = intent

        dynamoDb = DatabaseManager(context)
        userTaskRepository = UserTaskRepository(dynamoDb)
        taskDetailsRepository = TaskDetailsRepository(dynamoDb)

        searchForUncompletedUserTaskForNotification()
    }

    private fun searchForUncompletedUserTaskForNotification(): Job = launch {
        launch {
            when (val result = userTaskRepository.getUserInfo()) {
                is Result.Success<UserInfo> -> {
                    val uncompletedTasks = result.data.userTasks.filter { it.time.isNullOrEmpty() }
                    uncompletedTasks.isNotEmpty().let {
                        val uncompletedRandomTask = uncompletedTasks.random()
                        getUncompletedTaskDetails(result.data.group, uncompletedRandomTask.id)
                    }
                }
                else -> startNotificationService()
            }
        }
    }

    private fun getUncompletedTaskDetails(group: String, taskId: Int): Job = launch {
        when (val result = taskDetailsRepository.getTasksDetails(group, listOf(taskId))) {
            is Result.Success<List<TaskDetails>> -> {
                result.data.isNotEmpty().let {
                    startNotificationService(result.data[0])
                }
            }
            else -> startNotificationService()
        }
    }

    private fun startNotificationService(taskDetails: TaskDetails? = null) {
        val service = Intent(context, NotificationService::class.java)
            .putExtra(NotificationService.EXTRA_UNCOMPLETED_TASK, taskDetails)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(service))
        } else {
            context.startService(service)
        }
    }

}
