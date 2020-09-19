package com.github.aplikacjakardiologiczna.model.database.dynamodb

// TODO after migration move it to entity package
class UserTask(
    val id: Int,
    val index: Int,
    var time: String?,
    var taskDetails: TaskDetails
) {
    override fun toString(): String {
        return "UserTask [id: ${this.id}, index: ${this.index}, time: ${this.time}, taskDetails: ${this.taskDetails}]"
    }
}