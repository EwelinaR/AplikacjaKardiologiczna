package com.github.aplikacjakardiologiczna.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.TaskDataClass


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


    private fun generateList(size : Int) : List<TaskDataClass> {
        val list = ArrayList<TaskDataClass>()

        for(i in 0 until size) {
            val drawable = when (i % 4) {
                0 -> R.drawable.ic_bike
                1 -> R.drawable.ic_fitness_center
                2 -> R.drawable.ic_run
                else -> R.drawable.ic_pool
            }

            val task = TaskDataClass(drawable, "Task $i", "Line 2")
            list+=task
        }

        return list
    }
}
