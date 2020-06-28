package com.github.aplikacjakardiologiczna.model.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.github.aplikacjakardiologiczna.model.database.entity.UserTaskDetails
import java.util.*

@Dao
interface UserTaskDetailsDao {
    @Transaction
    @Query("SELECT * FROM Task INNER JOIN UserTask ON Task.id = UserTask.taskId WHERE UserTask.startDate BETWEEN :start AND :end")
    fun getAllInDate(start: Date, end: Date): List<UserTaskDetails>
}
