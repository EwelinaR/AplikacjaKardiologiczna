package com.github.aplikacjakardiologiczna.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Group(
    @ColumnInfo(name = "name") val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
