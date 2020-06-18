package com.github.aplikacjakardiologiczna.tasks

import android.util.Log
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.entity.Task
import com.github.aplikacjakardiologiczna.model.database.repository.TaskRepository
import com.github.aplikacjakardiologiczna.model.TaskView
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TasksPresenter(view: TasksContract.View, private val taskRepository: TaskRepository,
                     private val userTaskRepository: UserTaskRepository,
                     private val uiContext: CoroutineContext = Dispatchers.Main) : TasksContract.Presenter, CoroutineScope {

    private var view: TasksContract.View? = view
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    //TODO Replace this temporary tasks with the ones from DB
    private var tasks = ArrayList<TaskView>()

    override fun loadTasks() {

        // TODO Only for demonstration
        getAllTasks()

        tasks = arrayListOf(
                TaskView(R.drawable.ic_bike, "Task 1", "Line 2"),
                TaskView(R.drawable.ic_fitness_center, "Task 2", "Line 2"),
                TaskView(R.drawable.ic_run, "Task 3", "Line 2"),
                TaskView(R.drawable.ic_pool, "Task 4", "Line 2"))

        view?.onTasksLoaded()
    }

    /**
     * An example of getting data from db
     */
    private fun getAllTasks(): Job = launch {
        when (val result = taskRepository.getAllTasks()) {
            is Result.Success<List<Task>> -> {
                /* update UI when success */
                Log.d("getAllTasks", result.data.toString())
            }
            is Result.Error -> { /* update UI when error */ }
        }
    }

    override fun getTasksCount() = tasks.size

    override fun onBindTasksAtPosition(position: Int, itemView: TasksContract.TaskItemView) {
        val task = tasks[position]
        itemView.setImage(task.imageResource)
        itemView.setTextOnFirstLine(task.text1)
        itemView.setTextOnSecondLine(task.text2)
    }

    override fun onDestroy() {
        this.view = null
        job.cancel()
    }
}
