package com.github.aplikacjakardiologiczna.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE id IN (:taskIds)")
    fun loadAllByIds(taskIds: IntArray): List<Task>

    @Query("SELECT * FROM task WHERE task_name LIKE :t_name")
    fun findByName(t_name: String): Task

    @Insert
    fun insertAll(tasks: Task)

    @Delete
    fun delete(task: Task)
}
