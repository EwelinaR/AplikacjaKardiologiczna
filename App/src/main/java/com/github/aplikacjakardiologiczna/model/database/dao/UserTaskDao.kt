package com.github.aplikacjakardiologiczna.model.database.dao

import androidx.room.*
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask

@Dao
interface UserTaskDao {
    @Query("SELECT * FROM userTask")
    fun getAll(): List<UserTask>

    @Query("SELECT * FROM userTask WHERE id IN (:taskIds)")
    fun loadAllByIds(taskIds: IntArray): List<UserTask>

    @Query("SELECT * FROM userTask WHERE startDate IN (:date)")
    fun findByDate(date: Long): List<UserTask>

    @Query("SELECT taskId FROM userTask WHERE completionDateTime IS NOT NULL")
    fun findCompletedTaskIds(): List<Int>

    @Update
    fun update(task: UserTask)

    @Insert
    fun insertAll(tasks: List<UserTask>)

    @Insert
    fun insert(tasks: UserTask)

    @Delete
    fun delete(task: UserTask)
}
