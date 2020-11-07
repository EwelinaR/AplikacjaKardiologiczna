package com.github.aplikacjakardiologiczna.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.model.Message
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.repository.TaskDetailsRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_tasks.frame_layout_tasks
import kotlinx.android.synthetic.main.fragment_tasks.recycler_view_tasks
import kotlinx.android.synthetic.main.fragment_tasks.swipe_refresh_layout_tasks
import kotlinx.android.synthetic.main.fragment_tasks.text_view_no_tasks


class TasksFragment : Fragment(), TasksContract.View {

    private lateinit var presenter: TasksContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dynamoDb = DatabaseManager(requireContext())

        setPresenter(
            TasksPresenter(
                this,
                AppSettings(this.requireContext()),
                TaskDetailsRepository(dynamoDb),
                UserTaskRepository(dynamoDb)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onStart() {
        super.onStart()

        recycler_view_tasks.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recycler_view_tasks.adapter = TasksAdapter(presenter)

        swipe_refresh_layout_tasks.setOnRefreshListener { presenter.loadTasks() }
    }

    override fun onResume() {
        presenter.loadTasks()
        super.onResume()
    }

    override fun onTasksLoaded() {
        recycler_view_tasks.adapter?.notifyDataSetChanged()
    }

    override fun showNoTasks() {
        text_view_no_tasks.visibility = View.VISIBLE
        recycler_view_tasks.visibility = View.GONE
    }

    override fun showTasks() {
        text_view_no_tasks.visibility = View.GONE
        recycler_view_tasks.visibility = View.VISIBLE
    }

    override fun onTaskMoved(from: Int, to: Int) {
        recycler_view_tasks.adapter?.notifyItemMoved(from, to)
    }

    override fun showMessage(message: Message) {
        Snackbar
            .make(frame_layout_tasks, message.stringResourceId, Snackbar.LENGTH_LONG)
            .show()
    }

    override fun showLoading(isLoading: Boolean) {
        swipe_refresh_layout_tasks.isRefreshing = isLoading
    }

    override fun setPresenter(presenter: TasksContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
