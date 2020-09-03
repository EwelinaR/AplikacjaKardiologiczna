package com.github.aplikacjakardiologiczna.launcher

import com.github.aplikacjakardiologiczna.BasePresenter
import com.github.aplikacjakardiologiczna.BaseView

interface LauncherContract {
    interface View : BaseView<Presenter> {
        fun showMain()
        fun showLogin()
    }

    interface Presenter : BasePresenter {
        fun redirect()
    }
}
