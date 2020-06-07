package com.github.aplikacjakardiologiczna.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.aplikacjakardiologiczna.R
import kotlinx.android.synthetic.main.task_item.view.*


class TasksAdapter(private val presenter: TasksContract.Presenter) : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item,
                parent, false)

        return TasksViewHolder(presenter, itemView)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) =
            presenter.onBindTasksAtPosition(position, holder)

    override fun getItemCount() = presenter.getTasksCount()

    inner class TasksViewHolder(val presenter: TasksContract.Presenter, private val taskView: View) :
            RecyclerView.ViewHolder(taskView), TasksContract.TaskItemView {

        override fun setImage(resource: Int) {
            taskView.image_view_task_item.setImageResource(resource)
        }

        override fun setTextOnFirstLine(text: String) {
            taskView.text_view_task_item_line1.text = text
        }

        override fun setTextOnSecondLine(text: String) {
            taskView.text_view_task_item_line2.text = text
        }
    }
}
