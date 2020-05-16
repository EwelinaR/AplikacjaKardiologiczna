package com.github.aplikacjakardiologiczna.heart

class HeartPresenter(view: HeartContract.View) : HeartContract.Presenter {

    private var view: HeartContract.View? = view

    override fun onDestroy() {
        this.view = null
    }
}
