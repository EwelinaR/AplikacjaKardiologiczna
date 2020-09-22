package com.github.aplikacjakardiologiczna.tasks

import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dynamodb.UserTask
import com.github.aplikacjakardiologiczna.model.database.dynamodb.TaskDetails
import com.github.aplikacjakardiologiczna.model.database.dynamodb.UserInfo
import com.github.aplikacjakardiologiczna.model.database.repository.TaskDetailsRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class TasksPresenter(
    view: TasksContract.View,
    private val taskDetailsRepository: TaskDetailsRepository,
    private val userTaskRepository: UserTaskRepository,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : TasksContract.Presenter, CoroutineScope {

    private var view: TasksContract.View? = view
    private var job: Job = Job()
    private lateinit var userTasksForToday: UserInfo

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun loadTasks() {
        getUserInfo()
    }

    override fun getTasksCount() =
        if (this::userTasksForToday.isInitialized) userTasksForToday.userTasks.size else 0

    override fun onBindTasksAtPosition(position: Int, itemView: TasksContract.TaskItemView) {
        val task = userTasksForToday.userTasks[position]
        itemView.setImage(R.drawable.ic_run) //Category.valueOf(task.category).categoryIcon) // I AM GIVING UP
        itemView.setTaskName(task.taskDetails.name)
        itemView.setTaskDescription(task.taskDetails.description)
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
        val task = userTasksForToday.userTasks[position]
        task.time.let {
            task.time = if (isChecked) Calendar.getInstance().time.toString() else null
            updateUserTask(task, itemView)
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
                //TODO Show a snackbar/toast saying that something went wrong
            }
        }
    }

    private fun getTasksDetails(group: String, ids: List<Int>): Job = launch {
        when (val result = taskDetailsRepository.getTasksDetails(group, ids)) {
            is Result.Success<List<TaskDetails>> -> onTasksForTodayLoaded(result.data)
            is Result.Error -> {
                //TODO Show a snackbar/toast saying that something went wrong
            }
        }
    }

    private fun updateUserTask(task: UserTask, itemView: TasksContract.TaskItemView): Job = launch {
        val dbPosition = task.index
        when (dbPosition.let { userTaskRepository.updateUserTask(it) }) {
            is Result.Success -> onUserTaskUpdated(task, itemView)
            is Result.Error -> {
                //TODO Show a snackbar/toast saying that something went wrong
            }
        }
    }

    private fun onUserTaskUpdated(task: UserTask, itemView: TasksContract.TaskItemView) {
        val isTaskCompleted = task.time != null
        itemView.crossOffTask(isTaskCompleted)
        val moveTo = if (isTaskCompleted) (userTasksForToday.userTasks.size - 1) else 0
        moveTask(task, moveTo)
    }

    private fun onUserInfoLoaded(user: UserInfo) {
        this.userTasksForToday = user
        getTasksDetails(user.group, user.userTasks.map { it.id })
    }

    private fun onTasksForTodayLoaded(tasks: List<TaskDetails>) {
        userTasksForToday.userTasks.mapIndexed { index, task ->
            task.taskDetails = tasks[index]
        }
        view?.onTasksLoaded()
    }

    private fun moveTask(userTask: UserTask, to: Int) {
        val position = userTasksForToday.userTasks.indexOf(userTask)
        userTasksForToday.userTasks.removeAt(position)
        userTasksForToday.userTasks.add(to, userTask)

        view?.onTaskMoved(position, to)
    }
}
