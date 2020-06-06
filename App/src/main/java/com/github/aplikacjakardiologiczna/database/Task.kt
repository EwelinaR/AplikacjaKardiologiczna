package com.github.aplikacjakardiologiczna.database

import android.app.ActivityManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "task_name") val taskName: String?,
    @ColumnInfo(name = "task_description") val taskDescription: ActivityManager.TaskDescription,
    @ColumnInfo(name = "time_or_number") val isTime: Boolean,   // czy warunek wykonania zadania będzie podany w minutach czy w liczbie powtórzeń
    @ColumnInfo(name = "duration") val taskDuration: Int    // ile minut lub razy należy wykonywać zadanie
)
