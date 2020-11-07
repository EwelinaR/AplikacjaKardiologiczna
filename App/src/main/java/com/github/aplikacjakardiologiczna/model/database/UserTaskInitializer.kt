package com.github.aplikacjakardiologiczna.model.database

import com.github.aplikacjakardiologiczna.AppConstants
import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.extensions.CalendarExtensions.startOfToday
import com.github.aplikacjakardiologiczna.extensions.CalendarExtensions.startOfTomorrow
import com.github.aplikacjakardiologiczna.extensions.DateExtensions.polishDateFormat
import com.github.aplikacjakardiologiczna.model.database.entity.UserInfo
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import com.github.aplikacjakardiologiczna.model.database.repository.TaskDetailsRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class UserTaskInitializer(
    private val taskDetailsRepository: TaskDetailsRepository,
    private val userTaskRepository: UserTaskRepository,
    private val settings: AppSettings,
    private val callback: (Boolean) -> Unit
) : CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun initializeUserTasks(forToday: Boolean): Job = launch {
        when (val result = taskDetailsRepository.getTasksFromGroup()) {
            is Result.Success<List<Int>> -> onTasksLoaded(result.data, forToday)
            is Result.Error -> callback(false)
        }
    }

    private fun onTasksLoaded(tasks: List<Int>, forToday: Boolean) {
        val numberOfTasks = if (tasks.size < AppConstants.TASKS_PER_DAY) tasks.size else AppConstants.TASKS_PER_DAY
        val randomTasks = tasks.shuffled().subList(0, numberOfTasks)

        val calendar = Calendar.getInstance()
        val startDate = if (forToday) calendar.startOfToday else calendar.startOfTomorrow
        val formattedDate = startDate.polishDateFormat

        insertUserTasks(randomTasks, formattedDate)
    }

    private fun insertUserTasks(taskIds: List<Int>, startDate: String): Job = launch {
        val userTasks = ArrayList<UserTask>()
        taskIds.mapIndexedTo (userTasks, {
                index, taskId -> UserTask(taskId, index, null, null)
        })
        val userInfo = UserInfo(settings.username!!, settings.group!!, startDate, userTasks)

        val result = userTaskRepository.insertUserTasks(userInfo)
        callback(result is Result.Success<Unit>)
    }
}
