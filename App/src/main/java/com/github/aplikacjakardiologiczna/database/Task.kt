package com.github.aplikacjakardiologiczna.database

import android.app.ActivityManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "task_name") val name: String?,
    @ColumnInfo(name = "description") val description: ActivityManager.TaskDescription,
    @ColumnInfo(name = "time_or_number") val isTime: Boolean,   // is the task duration specified in minutes (true) or number of repeats (false)
    @ColumnInfo(name = "duration") val duration: Int
)
