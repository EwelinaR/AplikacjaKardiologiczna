package com.github.aplikacjakardiologiczna.main

import com.github.aplikacjakardiologiczna.AppConstants.TASKS_PER_DAY
import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.extensions.CalendarExtensions.atStartOfDay
import com.github.aplikacjakardiologiczna.model.database.Result
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

class MainPresenter(
    view: MainContract.View,
    private val settings: AppSettings,
    private val taskRepository: TaskRepository,
    private val userTaskRepository: UserTaskRepository,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : MainContract.Presenter, CoroutineScope {

    private var view: MainContract.View? = view
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun onViewCreated() {
        view?.showHeartView()

        if (settings.firstRun) {
            initializeFirstUserTasks()
        }
    }

    override fun onHeartTabClicked() {
        view?.showHeartView()
    }

    override fun onTasksTabClicked() {
        view?.showTasksView()
    }

    override fun onDestroy() {
        this.view = null
    }

    private fun initializeFirstUserTasks(): Job = launch {
        when (val result = taskRepository.getAllTasks()) {
            is Result.Success<List<Task>> -> onTasksLoaded(result.data)
            is Result.Error -> {
                //TODO Show a snackbar/toast saying that something went wrong
            }
        }
    }

    private fun onTasksLoaded(tasks: List<Task>) {
        val numberOfTasks = if (tasks.size < TASKS_PER_DAY) tasks.size else TASKS_PER_DAY
        val randomTasks = tasks.shuffled().subList(0, numberOfTasks)

        val todaysDate = Calendar.getInstance().time
        val todaysDateStart = Calendar.getInstance().atStartOfDay(todaysDate)

        val userTasks = Array(TASKS_PER_DAY) { i ->
            UserTask(
                taskId = randomTasks[i].id,
                startDate = todaysDateStart
            )
        }.toList()

        insertUserTasks(userTasks)
    }

    private fun insertUserTasks(userTasks: List<UserTask>): Job = launch {
        when (userTaskRepository.insertUserTasks(userTasks)) {
            is Result.Success<Unit> -> settings.firstRun = false
            is Result.Error -> {
                //TODO Show a snackbar/toast saying that something went wrong
            }
        }
    }
}
