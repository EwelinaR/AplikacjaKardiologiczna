package com.github.aplikacjakardiologiczna.main

import com.github.aplikacjakardiologiczna.BasePresenter

class MainPresenter(view: MainContract.View) : BasePresenter<MainContract.View>(view),
    MainContract.Presenter {
}
