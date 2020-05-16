package com.github.aplikacjakardiologiczna.heart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.github.aplikacjakardiologiczna.R


class HeartFragment : Fragment(), HeartContract.View {

    private lateinit var presenter: HeartContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setPresenter(HeartPresenter(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_heart, container, false)
    }

    override fun setPresenter(presenter: HeartContract.Presenter) {
        this.presenter = presenter
    }
}
