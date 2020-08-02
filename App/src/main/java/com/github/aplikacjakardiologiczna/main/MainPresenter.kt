package com.github.aplikacjakardiologiczna.main

import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.model.database.UserTaskInitializer
import com.github.aplikacjakardiologiczna.model.database.repository.TaskRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private fun initializeFirstUserTasks() {
        val taskInitializer = UserTaskInitializer(
            taskRepository,
            userTaskRepository,
            ::initializeFirstUserTasksCallback
        )
        taskInitializer.initializeUserTasks(true)
    }

    private fun initializeFirstUserTasksCallback(wasSuccessful: Boolean) {
        when (wasSuccessful) {
            true -> settings.firstRun = false
            false -> {
                // TODO Do sth
            }
        }
    }
}
