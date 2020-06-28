package com.github.aplikacjakardiologiczna.tasks

import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.entity.UserTaskDetails
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TasksPresenter(view: TasksContract.View,
                     private val userTaskDetailsRepository: UserTaskDetailsRepository,
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
        itemView.setTextOnFirstLine(task.details.name)
        itemView.setTextOnSecondLine(task.details.description)
    }

    override fun onDestroy() {
        this.view = null
        job.cancel()
    }

    private fun getTasksForToday(): Job = launch {
        when (val result = userTaskDetailsRepository.getTasksForToday()) {
            is Result.Success<List<UserTaskDetails>> -> {
                onTasksForTodayLoaded(result.data)
            }
            is Result.Error -> { /* update UI when error */ }
        }
    }

    private fun onTasksForTodayLoaded(tasks: List<UserTaskDetails>) {
        tasksForToday = ArrayList(tasks)
        view?.onTasksLoaded()
    }
}
