package com.github.aplikacjakardiologiczna.model.database.entity

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.*
import java.util.*

@Entity
data class UserTask(
        @ForeignKey
        (entity = Task::class,
                parentColumns = ["id"],
                childColumns = ["taskId"],
                onDelete = ForeignKey.NO_ACTION)
        @NonNull @ColumnInfo(name = "taskId") val taskId: Int,
        @NonNull @ColumnInfo(name = "startDate") val startDate: Date,
        @Nullable @ColumnInfo(name = "completionDateTime") val completionDateTime: Date? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
