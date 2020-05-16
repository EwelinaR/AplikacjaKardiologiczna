package com.github.aplikacjakardiologiczna.tasks

import com.github.aplikacjakardiologiczna.BasePresenter
import com.github.aplikacjakardiologiczna.BaseView

interface TasksContract {
    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

    }
}
