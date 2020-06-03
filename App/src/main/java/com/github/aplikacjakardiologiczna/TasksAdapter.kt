package com.github.aplikacjakardiologiczna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_item.view.*

class TasksAdapter(private val tasksList : List<TaskDataClass>) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item,
            parent, false)

        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasksList[position]

        holder.imageView.setImageResource(currentTask.imageResource)
        holder.textView1.text = currentTask.text1
        holder.textView2.text = currentTask.text2
    }

    override fun getItemCount() = tasksList.size

    class TaskViewHolder(taskView : View) : RecyclerView.ViewHolder(taskView) {
        val imageView : ImageView = taskView.image_view
        val textView1 : TextView = itemView.text_view_1
        val textView2 : TextView = itemView.text_view_2
    }
}