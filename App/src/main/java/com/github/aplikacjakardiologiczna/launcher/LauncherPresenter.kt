package com.github.aplikacjakardiologiczna.launcher

import com.github.aplikacjakardiologiczna.AppSettings

class LauncherPresenter(
    view: LauncherContract.View,
    private val settings: AppSettings
) : LauncherContract.Presenter {

    private var view: LauncherContract.View? = view

    override fun redirect() {
        if (isAppNotSetUp()) {
            view?.showSetUp()
        } else {
            view?.showMain()
        }
    }

    override fun onDestroy() {
        this.view = null
    }

    private fun isAppNotSetUp(): Boolean =
        settings.firstRun || settings.username == null || settings.groupId < 0

}
