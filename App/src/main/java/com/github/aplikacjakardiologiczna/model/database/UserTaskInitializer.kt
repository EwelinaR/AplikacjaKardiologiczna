package com.github.aplikacjakardiologiczna.model.database

import com.github.aplikacjakardiologiczna.AppConstants
import com.github.aplikacjakardiologiczna.extensions.CalendarExtensions.today
import com.github.aplikacjakardiologiczna.extensions.CalendarExtensions.tomorrow
import com.github.aplikacjakardiologiczna.model.database.entity.Task
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import com.github.aplikacjakardiologiczna.model.database.repository.TaskRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class UserTaskInitializer(
    private val taskRepository: TaskRepository,
    private val userTaskRepository: UserTaskRepository,
    private val callback: (Boolean) -> Unit
) : CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun initializeUserTasks(forToday: Boolean): Job = launch {
        when (val result = taskRepository.getAllTasks()) {
            is Result.Success<List<Task>> -> onTasksLoaded(result.data, forToday)
            is Result.Error -> callback(false)
        }
    }

    private fun onTasksLoaded(tasks: List<Task>, forToday: Boolean) {
        val numberOfTasks = if (tasks.size < AppConstants.TASKS_PER_DAY) tasks.size else AppConstants.TASKS_PER_DAY
        val randomTasks = tasks.shuffled().subList(0, numberOfTasks)

        val calendar = Calendar.getInstance()
        val startDate = if (forToday) calendar.today else calendar.tomorrow

        val userTasks = randomTasks.map {UserTask(it.id, startDate)}

        insertUserTasks(userTasks)
    }

    private fun insertUserTasks(userTasks: List<UserTask>): Job = launch {
        val result = userTaskRepository.insertUserTasks(userTasks)
        callback(result is Result.Success<Unit>)
    }
}
