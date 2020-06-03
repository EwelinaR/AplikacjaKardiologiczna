package com.github.aplikacjakardiologiczna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_pattern.view.*

class PatternAdapter(private val tasksList : List<TaskPattern>) : RecyclerView.Adapter<PatternAdapter.PatternViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatternViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_pattern,
            parent, false)

        return PatternViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PatternViewHolder, position: Int) {
        val currentTask = tasksList[position]

        holder.imageView.setImageResource(currentTask.imageResource)
        holder.textView1.text = currentTask.text1
        holder.textView2.text = currentTask.text2
    }

    override fun getItemCount() = tasksList.size

    class PatternViewHolder(taskView : View) : RecyclerView.ViewHolder(taskView) {
        val imageView : ImageView = taskView.image_view
        val textView1 : TextView = itemView.text_view_1
        val textView2 : TextView = itemView.text_view_2
    }
}