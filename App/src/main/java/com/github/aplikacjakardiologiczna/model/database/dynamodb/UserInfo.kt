package com.github.aplikacjakardiologiczna.model.database.dynamodb

// TODO after migration move it to entity package
class UserInfo(
    val nick: String,
    val group: String,
    var userTasks: ArrayList<UserTask>

) {
    override fun toString(): String {
        return "UserInfo [nick: ${this.nick}, group: ${this.group}, userTasks: ${this.userTasks}]"
    }
}




