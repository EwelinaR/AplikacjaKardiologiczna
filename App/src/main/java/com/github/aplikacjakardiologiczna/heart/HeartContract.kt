package com.github.aplikacjakardiologiczna.heart

import com.github.aplikacjakardiologiczna.BasePresenter
import com.github.aplikacjakardiologiczna.BaseView

interface HeartContract {
    interface View : BaseView<Presenter> {
        fun showProgressBar(percent: Int)
    }

    interface Presenter : BasePresenter {
        fun onCreateView()
    }
}
