package com.github.aplikacjakardiologiczna.model.database.entity

class UserTask(
    val id: Int,
    val index: Int,
    var time: String?,
    var taskDetails: TaskDetails?
) {
    override fun toString(): String {
        return "UserTask [id: ${this.id}, index: ${this.index}, time: ${this.time}, taskDetails: ${this.taskDetails}]"
    }
}