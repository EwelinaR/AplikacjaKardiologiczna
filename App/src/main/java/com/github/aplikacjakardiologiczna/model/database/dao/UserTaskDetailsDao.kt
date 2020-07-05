package com.github.aplikacjakardiologiczna.model.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.github.aplikacjakardiologiczna.model.database.entity.UserTaskDetails
import java.util.Date

@Dao
interface UserTaskDetailsDao {
    @Transaction
    @Query("SELECT * FROM UserTask INNER JOIN Task ON UserTask.taskId = Task.id WHERE UserTask.startDate IN (:startDate)")
    fun getByStartDate(startDate: Date): List<UserTaskDetails>
}
