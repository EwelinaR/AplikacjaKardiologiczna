package com.github.aplikacjakardiologiczna.heart

import com.github.aplikacjakardiologiczna.BasePresenter
import com.github.aplikacjakardiologiczna.BaseView

interface HeartContract {
    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

    }
}
