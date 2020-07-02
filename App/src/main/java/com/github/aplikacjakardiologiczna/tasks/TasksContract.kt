package com.github.aplikacjakardiologiczna.tasks

import com.github.aplikacjakardiologiczna.BasePresenter
import com.github.aplikacjakardiologiczna.BaseView

interface TasksContract {
    interface View : BaseView<Presenter> {
        fun onTasksLoaded()
    }

    interface TaskItemView {
        fun setImage(resource: Int)
        fun setTaskName(text: String)
        fun setTaskDescription(text: String)
    }

    interface Presenter : BasePresenter {
        fun loadTasks()
        fun getTasksCount(): Int
        fun onBindTasksAtPosition(position: Int, itemView: TaskItemView)
    }
}
