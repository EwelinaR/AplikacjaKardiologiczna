package com.github.aplikacjakardiologiczna.model.database.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupTask(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @NonNull val groupId: Int,
    @NonNull val taskId: Int
)
