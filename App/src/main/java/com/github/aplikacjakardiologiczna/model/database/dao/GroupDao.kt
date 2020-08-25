package com.github.aplikacjakardiologiczna.model.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.github.aplikacjakardiologiczna.model.database.entity.Group

@Dao
interface GroupDao {
    @Query("SELECT * FROM `group`")
    fun getAll(): List<Group>
}
