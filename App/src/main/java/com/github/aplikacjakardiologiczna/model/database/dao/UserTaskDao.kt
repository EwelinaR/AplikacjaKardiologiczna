package com.github.aplikacjakardiologiczna.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import java.util.Date

@Dao
interface UserTaskDao {
    @Query("SELECT * FROM userTask")
    fun getAll(): List<UserTask>

    @Query("SELECT * FROM userTask WHERE id IN (:taskIds)")
    fun loadAllByIds(taskIds: IntArray): List<UserTask>

    @Query("SELECT * FROM userTask WHERE startDate IN (:date)")
    fun findByDate(date: Date): List<UserTask>

    @Query("SELECT taskId FROM userTask WHERE completionDateTime IS NOT NULL")
    fun findCompletedTaskIds(): List<Int>

    @Query("SELECT count(taskId) FROM userTask WHERE startDate IN (:date)")
    fun countTasks(date: Date): Int

    @Update
    fun update(task: UserTask)

    @Insert
    fun insertAll(tasks: List<UserTask>)

    @Insert
    fun insert(tasks: UserTask)

    @Delete
    fun delete(task: UserTask)
}
