package com.github.aplikacjakardiologiczna.model.database.entity

import java.io.Serializable

class TaskDetails(
    val group: String,
    val id: Int,
    val name: String,
    val category: String,
    val description: String
) : Serializable {
    override fun toString(): String {
        return "TaskDetails [group: ${this.group}, id: ${this.id}, name: ${this.name}, category: ${this.category}, description: ${this.description}]"
    }
}
