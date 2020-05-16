package com.github.aplikacjakardiologiczna.tasks

class TasksPresenter(view: TasksContract.View) : TasksContract.Presenter {

    private var view: TasksContract.View? = view

    override fun onDestroy() {
        this.view = null
    }
}
