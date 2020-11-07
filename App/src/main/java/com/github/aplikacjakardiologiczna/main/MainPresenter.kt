package com.github.aplikacjakardiologiczna.main

import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.notification.NotificationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainPresenter(
    view: MainContract.View,
    private val settings: AppSettings,
    private val notificationUtils: NotificationUtils,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : MainContract.Presenter, CoroutineScope {

    private var view: MainContract.View? = view
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun onViewCreated() {
        if (settings.username != null && settings.group != null) {
            view?.showHeartView()
            if (!notificationUtils.isAlarmUp())
                notificationUtils.setAlarm()
        } else {
            logout()
        }
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

        if (notificationUtils.isAlarmUp())
            notificationUtils.cancelAlarm()

        view?.showLogin()
    }

    override fun onDestroy() {
        this.view = null
    }
}
