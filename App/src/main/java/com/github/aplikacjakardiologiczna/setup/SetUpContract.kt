package com.github.aplikacjakardiologiczna.setup

import com.github.aplikacjakardiologiczna.BasePresenter
import com.github.aplikacjakardiologiczna.BaseView
import com.github.aplikacjakardiologiczna.model.ErrorMessage

interface SetUpContract {
    interface View : BaseView<Presenter> {
        fun onGroupsLoaded(groups: List<String>)
        fun showMain()
        fun showValidationError(errorMessage: ErrorMessage)
    }

    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun onConfirmButtonPressed(username: String, groupPosition: Int)
    }
}
