package com.github.aplikacjakardiologiczna.main

import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.model.database.repository.TaskDetailsRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainPresenter(
    view: MainContract.View,
    private val settings: AppSettings,
    private val taskDetailsRepository: TaskDetailsRepository,
    private val userTaskRepository: UserTaskRepository,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : MainContract.Presenter, CoroutineScope {

    private var view: MainContract.View? = view
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun onViewCreated() {
        view?.showHeartView()
    }

    override fun onHeartTabClicked() {
        view?.showHeartView()
    }

    override fun onTasksTabClicked() {
        view?.showTasksView()
    }

    override fun logout() {
        settings.username = null
        settings.group = null

        view?.showLogin()
    }

    override fun onDestroy() {
        this.view = null
    }
}
