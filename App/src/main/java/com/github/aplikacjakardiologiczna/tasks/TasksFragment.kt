package com.github.aplikacjakardiologiczna.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.github.aplikacjakardiologiczna.R


class TasksFragment : Fragment(), TasksContract.View {

    private lateinit var presenter: TasksContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setPresenter(TasksPresenter(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun setPresenter(presenter: TasksContract.Presenter) {
        this.presenter = presenter
    }
}
