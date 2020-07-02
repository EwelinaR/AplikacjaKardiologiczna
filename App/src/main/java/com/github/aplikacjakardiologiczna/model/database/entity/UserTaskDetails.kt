package com.github.aplikacjakardiologiczna.model.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserTaskDetails(
        @Embedded val details: Task,
        @Relation(
                parentColumn = "id",
                entityColumn = "taskId"
        )
        val userTask: UserTask?
)
