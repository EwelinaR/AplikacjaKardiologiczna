package com.github.aplikacjakardiologiczna.tasks

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.aplikacjakardiologiczna.R
import kotlinx.android.synthetic.main.item_task.view.checkbox_item_task
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

        init {
            taskView.checkbox_item_task.setOnClickListener {
                val isChecked = (it as CheckBox).isChecked
                crossOffTask(isChecked)
                presenter.onTaskChecked(adapterPosition, isChecked, this)

                taskView.checkbox_item_task.isEnabled = false
            }
        }

        override fun setImage(resource: Int) {
            taskView.image_view_item_task.setImageResource(resource)
        }

        override fun setTaskName(text: String) {
            taskView.text_view_item_task_name.text = text
        }

        override fun setTaskDescription(text: String) {
            taskView.text_view_item_task_description.text = text
        }

        override fun crossOffTask(shouldCrossOff: Boolean) {
            if (shouldCrossOff) {
                crossOffLine(taskView.text_view_item_task_name)
                crossOffLine(taskView.text_view_item_task_description)
            } else {
                uncrossLine(taskView.text_view_item_task_name)
                uncrossLine(taskView.text_view_item_task_description)
            }
        }

        override fun checkTask(shouldBeChecked: Boolean) {
            taskView.checkbox_item_task.isChecked = shouldBeChecked
            taskView.checkbox_item_task.isEnabled = !shouldBeChecked
        }

        private fun crossOffLine(textView: TextView) {
            textView.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

        private fun uncrossLine(textView: TextView) {
            textView.apply {
                paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }
}
