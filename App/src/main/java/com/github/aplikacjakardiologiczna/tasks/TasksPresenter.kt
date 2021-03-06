package com.github.aplikacjakardiologiczna.tasks

import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.extensions.CalendarExtensions.now
import com.github.aplikacjakardiologiczna.extensions.DateExtensions.polishTimeFormat
import com.github.aplikacjakardiologiczna.model.Message
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.UserTaskInitializer
import com.github.aplikacjakardiologiczna.model.database.converter.CategoryConverter
import com.github.aplikacjakardiologiczna.model.database.entity.TaskDetails
import com.github.aplikacjakardiologiczna.model.database.entity.UserInfo
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import com.github.aplikacjakardiologiczna.model.database.repository.TaskDetailsRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class TasksPresenter(
    view: TasksContract.View,
    private val settings: AppSettings,
    private val taskDetailsRepository: TaskDetailsRepository,
    private val userTaskRepository: UserTaskRepository,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : TasksContract.Presenter, CoroutineScope {

    private var view: TasksContract.View? = view
    private var job: Job = Job()
    private var tasksSuccessfullyInitialized = false
    private lateinit var userInfo: UserInfo

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun loadTasks() {
        view?.showLoading(true)
        getUserInfo()
    }

    override fun getTasksCount() =
        if (this::userInfo.isInitialized) userInfo.userTasks.size else 0

    override fun onBindTasksAtPosition(position: Int, itemView: TasksContract.TaskItemView) {
        val task = userInfo.userTasks[position]
        task.taskDetails?.category?.let {
            CategoryConverter.toCategory(it)?.let { it1 ->
                itemView.setImage(it1)
            }
        }
        task.taskDetails?.let {
            itemView.setTaskName(it.name)
            itemView.setTaskDescription(it.description)
        }

        task.time?.let {
            itemView.crossOffTask(true)
            itemView.checkTask(true)
        }
    }

    override fun onTaskChecked(
        position: Int,
        isChecked: Boolean,
        itemView: TasksContract.TaskItemView
    ) {
        val task = userInfo.userTasks[position]
        task.let {
            task.time = if (isChecked) Calendar.getInstance().now.polishTimeFormat else null
            completeUserTask(task, itemView)
        }
    }

    override fun onDestroy() {
        this.view = null
        job.cancel()
    }

    private fun getUserInfo(): Job = launch {
        when (val result = userTaskRepository.getUserInfo()) {
            is Result.Success<UserInfo> -> onUserInfoLoaded(result.data)
            is Result.Error -> {
                if (!tasksSuccessfullyInitialized) {
                    initializeUserTasksForToday()
                } else handleTasksLoadingError()
            }
        }
    }

    private fun initializeUserTasksForToday() {
        val taskInitializer = UserTaskInitializer(
            taskDetailsRepository,
            userTaskRepository,
            settings,
            ::initializeUserTasksCallback
        )
        taskInitializer.initializeUserTasks(true)
    }

    private fun initializeUserTasksCallback(wasSuccessful: Boolean) {
        if (wasSuccessful) {
            tasksSuccessfullyInitialized = true
            getUserInfo()
        }
    }

    private fun getTasksDetails(ids: List<Int>): Job = launch {
        when (val result = taskDetailsRepository.getTasksDetails(ids)) {
            is Result.Success<List<TaskDetails>> -> onTasksForTodayLoaded(result.data)
            is Result.Error -> handleTasksLoadingError()
        }
    }

    private fun completeUserTask(task: UserTask, itemView: TasksContract.TaskItemView): Job =
        launch {
            val dbPosition = task.index
            when (dbPosition.let { userTaskRepository.completeUserTask(it) }) {
                is Result.Success -> onUserTaskCompleted(task, itemView)
                is Result.Error -> {
                    withContext(Dispatchers.Main) {
                        view?.showMessage(Message.GENERIC_ERROR_MESSAGE)
                    }
                }
            }
        }

    private fun onUserTaskCompleted(task: UserTask, itemView: TasksContract.TaskItemView) {
        val isTaskCompleted = task.time != null
        itemView.crossOffTask(isTaskCompleted)
        val moveTo = if (isTaskCompleted) (userInfo.userTasks.size - 1) else 0
        moveTask(task, moveTo)
    }

    private fun onUserInfoLoaded(userInfo: UserInfo) {
        this.userInfo = userInfo
        getTasksDetails(userInfo.userTasks.map { it.id })
    }

    private fun onTasksForTodayLoaded(tasks: List<TaskDetails>) {
        userInfo.userTasks.mapIndexed { index, task ->
            task.taskDetails = tasks[index]
        }
        userInfo.userTasks.sortBy { it.time }
        view?.onTasksLoaded()
        view?.showLoading(false)
        view?.showTasks()
    }

    private fun moveTask(userTask: UserTask, to: Int) {
        val position = userInfo.userTasks.indexOf(userTask)
        userInfo.userTasks.removeAt(position)
        userInfo.userTasks.add(to, userTask)

        view?.onTaskMoved(position, to)
    }

    private fun handleTasksLoadingError() {
        view?.showMessage(Message.GENERIC_ERROR_MESSAGE)
        view?.showLoading(false)
        view?.showNoTasks()
    }
}
