package com.github.aplikacjakardiologiczna.model.database.entity

class UserInfo(
    val nick: String,
    val group: String,
    var date: String,
    var time: String,
    var userTasks: ArrayList<UserTask>

) {
    var id: Long = 0

    override fun toString(): String {
        return "UserInfo [id: ${this.id}, nick: ${this.nick}, group: ${this.group}, date: ${this.date}, time: ${this.time}, userTasks: ${this.userTasks}]"
    }
}
