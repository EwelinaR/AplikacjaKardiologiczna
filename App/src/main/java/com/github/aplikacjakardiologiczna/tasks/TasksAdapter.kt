package com.github.aplikacjakardiologiczna.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.aplikacjakardiologiczna.R
import kotlinx.android.synthetic.main.item_task.view.image_view_item_task
import kotlinx.android.synthetic.main.item_task.view.text_view_item_task_description
import kotlinx.android.synthetic.main.item_task.view.text_view_item_task_name


class TasksAdapter(private val presenter: TasksContract.Presenter) : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task,
                parent, false)

        return TasksViewHolder(presenter, itemView)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) =
            presenter.onBindTasksAtPosition(position, holder)

    override fun getItemCount() = presenter.getTasksCount()

    inner class TasksViewHolder(val presenter: TasksContract.Presenter, private val taskView: View) :
            RecyclerView.ViewHolder(taskView), TasksContract.TaskItemView {

        override fun setImage(resource: Int) {
            taskView.image_view_item_task.setImageResource(resource)
        }

        override fun setTaskName(text: String) {
            taskView.text_view_item_task_name.text = text
        }

        override fun setTaskDescription(text: String) {
            taskView.text_view_item_task_description.text = text
        }
    }
}
