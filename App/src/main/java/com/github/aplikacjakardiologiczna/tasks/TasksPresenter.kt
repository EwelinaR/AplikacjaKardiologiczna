package com.github.aplikacjakardiologiczna.tasks

import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.model.Task

class TasksPresenter(view: TasksContract.View) : TasksContract.Presenter {

    private var view: TasksContract.View? = view

    //TODO Replace this temporary tasks with the ones from DB
    private var tasks = ArrayList<Task>()

    override fun loadTasks() {
        tasks = arrayListOf(
                Task(R.drawable.ic_bike, "Task 1", "Line 2"),
                Task(R.drawable.ic_fitness_center, "Task 2", "Line 2"),
                Task(R.drawable.ic_run, "Task 3", "Line 2"),
                Task(R.drawable.ic_pool, "Task 4", "Line 2"))

        view?.onTasksLoaded()
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
    }
}
