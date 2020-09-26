package com.github.aplikacjakardiologiczna.model.database.entity

// TODO after migration move it to entity package
class UserInfo(
    val nick: String,
    val group: String,
    var date: String,
    var userTasks: ArrayList<UserTask>

) {
    override fun toString(): String {
        return "UserInfo [nick: ${this.nick}, group: ${this.group}, date: ${this.date}, userTasks: ${this.userTasks}]"
    }
}
