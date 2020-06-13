package com.github.aplikacjakardiologiczna.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.aplikacjakardiologiczna.model.database.Category

@Entity
data class Task(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "isTime") val isTime: Boolean,   // is the task duration specified in minutes (true) or number of repeats (false)
    @ColumnInfo(name = "duration") val duration: Int,
    @ColumnInfo(name = "category") val category: Category
)

