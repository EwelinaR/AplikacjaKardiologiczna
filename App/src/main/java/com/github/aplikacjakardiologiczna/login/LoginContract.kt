package com.github.aplikacjakardiologiczna.login

import com.github.aplikacjakardiologiczna.BasePresenter
import com.github.aplikacjakardiologiczna.BaseView
import com.github.aplikacjakardiologiczna.model.Message

interface LoginContract {
    interface View : BaseView<Presenter> {
        fun showMain()
        fun showValidationError(message: Message)
    }

    interface Presenter : BasePresenter {
        fun onConfirmButtonPressed(username: String)
    }
}
