package com.github.aplikacjakardiologiczna.main

class MainPresenter(view: MainContract.View) : MainContract.Presenter {

    private var view: MainContract.View? = view

    override fun onViewCreated() {
        view?.showHeartView()
    }

    override fun onHeartTabClicked() {
        view?.showHeartView()
    }

    override fun onTasksTabClicked() {
        view?.showTasksView()
    }

    override fun onDestroy() {
        this.view = null
    }
}
