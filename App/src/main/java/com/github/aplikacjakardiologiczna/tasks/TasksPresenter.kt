package com.github.aplikacjakardiologiczna.tasks

import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.entity.UserTaskDetails
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskDetailsRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class TasksPresenter(view: TasksContract.View,
                     private val userTaskDetailsRepository: UserTaskDetailsRepository,
                     private val userTaskRepository: UserTaskRepository,
                     private val uiContext: CoroutineContext = Dispatchers.Main) : TasksContract.Presenter, CoroutineScope {

    private var view: TasksContract.View? = view
    private var job: Job = Job()
    private var tasksForToday = ArrayList<UserTaskDetails>()

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun loadTasks() {
        getTasksForToday()
    }

    override fun getTasksCount() = tasksForToday.size

    override fun onBindTasksAtPosition(position: Int, itemView: TasksContract.TaskItemView) {
        val task = tasksForToday[position]
        itemView.setImage(task.details.category.categoryIcon)
        itemView.setTaskName(task.details.name)
        itemView.setTaskDescription(task.details.description)
        task.userTask?.completionDateTime?.let {
            itemView.crossOffTask(true)
            itemView.checkTask(true)
        }
    }

    override fun onTaskChecked(position: Int, isChecked: Boolean, itemView: TasksContract.TaskItemView) {
        val task = tasksForToday[position]
        task.userTask?.completionDateTime = if (isChecked) Calendar.getInstance().time else null
        updateUserTask(task, itemView)
    }

    override fun onDestroy() {
        this.view = null
        job.cancel()
    }

    private fun getTasksForToday(): Job = launch {
        when (val result = userTaskDetailsRepository.getTasksForToday()) {
            is Result.Success<List<UserTaskDetails>> -> onTasksForTodayLoaded(result.data)
            is Result.Error -> {
                //TODO Show a snackbar/toast saying that something went wrong
            }
        }
    }

    private fun updateUserTask(task: UserTaskDetails, itemView: TasksContract.TaskItemView): Job = launch {
        when (task.userTask?.let { userTaskRepository.updateUserTask(it) }) {
            is Result.Success -> onUserTaskUpdated(task, itemView)
            is Result.Error -> {
                //TODO Show a snackbar/toast saying that something went wrong
                getTasksForToday()
            }
        }
    }

    private fun onUserTaskUpdated(task: UserTaskDetails, itemView: TasksContract.TaskItemView) {
        val isTaskCompleted = task.userTask?.completionDateTime != null
        itemView.crossOffTask(isTaskCompleted)

        val moveTo = if (isTaskCompleted) (tasksForToday.size - 1) else 0
        moveTask(task, moveTo)
    }

    private fun onTasksForTodayLoaded(tasks: List<UserTaskDetails>) {
        tasksForToday = ArrayList(tasks)
        view?.onTasksLoaded()
    }

    private fun moveTask(task: UserTaskDetails, to: Int) {
        val position = tasksForToday.indexOf(task)
        tasksForToday.removeAt(position)
        tasksForToday.add(to, task)

        view?.onTaskMoved(position, to)
    }
}
