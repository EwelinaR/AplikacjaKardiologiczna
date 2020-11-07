package com.github.aplikacjakardiologiczna.login

import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.model.ErrorMessage
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginPresenter(
    view: LoginContract.View,
    private val settings: AppSettings,
    private val userTaskRepository: UserTaskRepository,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : LoginContract.Presenter, CoroutineScope {

    private var view: LoginContract.View? = view
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun onConfirmButtonPressed(username: String) {
        if (username.isNotEmpty()) {
            checkUser(username)
        } else {
            view?.showValidationError(ErrorMessage.VALIDATION_ERROR_EMPTY_USERNAME)
        }
    }

    private fun checkUser(username: String): Job = launch {
        when (val result = userTaskRepository.getUserGroup(username)) {
            is Result.Success<String> -> successfullyLoggedIn(username, result.data)
            is Result.Error -> {
                view?.showValidationError(ErrorMessage.VALIDATION_ERROR_WRONG_USERNAME)
            }
        }
    }

    private fun successfullyLoggedIn(username: String, group: String) {
        settings.username = username
        settings.group = group
        view?.showMain()
    }

    override fun onDestroy() {
        this.view = null
    }
}
