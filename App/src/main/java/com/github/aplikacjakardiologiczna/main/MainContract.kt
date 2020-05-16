package com.github.aplikacjakardiologiczna.main

import com.github.aplikacjakardiologiczna.BasePresenter
import com.github.aplikacjakardiologiczna.BaseView

interface MainContract {
    interface View : BaseView<Presenter> {
        fun showHeartView()
        fun showTasksView()
    }

    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun onHeartTabClicked()
        fun onTasksTabClicked()
    }
}
