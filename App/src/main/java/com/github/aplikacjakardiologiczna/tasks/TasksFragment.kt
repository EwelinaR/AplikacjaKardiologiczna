package com.github.aplikacjakardiologiczna.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.model.database.AppDatabase
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskDetailsRepository
import kotlinx.android.synthetic.main.fragment_tasks.*


class TasksFragment : Fragment(), TasksContract.View {

    private lateinit var presenter: TasksContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(requireContext())

        setPresenter(TasksPresenter(this,
                UserTaskDetailsRepository.getInstance(db.userTaskDetailsDao())))

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onStart() {
        super.onStart()

        recycler_view_tasks.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recycler_view_tasks.adapter = TasksAdapter(presenter)

        presenter.loadTasks()
    }

    override fun onTasksLoaded() {
        recycler_view_tasks.adapter?.notifyDataSetChanged()
    }

    override fun setPresenter(presenter: TasksContract.Presenter) {
        this.presenter = presenter
    }
}
