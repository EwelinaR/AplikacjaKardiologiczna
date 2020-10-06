package com.github.aplikacjakardiologiczna.login

import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.model.ErrorMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class LoginPresenter(
    view: LoginContract.View,
    private val settings: AppSettings,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : LoginContract.Presenter, CoroutineScope {

    private var view: LoginContract.View? = view
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun onConfirmButtonPressed(username: String) {
        if (username.isNotEmpty()) {
            settings.username = username
            view?.showMain()
        } else {
            view?.showValidationError(ErrorMessage.VALIDATION_ERROR_EMPTY_USERNAME)
        }
    }

    override fun onDestroy() {
        this.view = null
    }
}
