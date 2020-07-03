package com.github.aplikacjakardiologiczna.tasks

import com.github.aplikacjakardiologiczna.BasePresenter
import com.github.aplikacjakardiologiczna.BaseView

interface TasksContract {
    interface View : BaseView<Presenter> {
        fun onTasksLoaded()
        fun onTaskMoved(from: Int, to: Int)
    }

    interface TaskItemView {
        fun setImage(resource: Int)
        fun setTaskName(text: String)
        fun setTaskDescription(text: String)
        fun crossOffTask(shouldCrossOff: Boolean)
        fun checkTask(shouldBeChecked: Boolean)
    }

    interface Presenter : BasePresenter {
        fun loadTasks()
        fun getTasksCount(): Int
        fun onBindTasksAtPosition(position: Int, itemView: TaskItemView)
        fun onTaskChecked(position: Int, isChecked: Boolean, itemView: TaskItemView)
    }
}
