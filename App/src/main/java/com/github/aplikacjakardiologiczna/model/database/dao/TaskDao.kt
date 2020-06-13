package com.github.aplikacjakardiologiczna.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.aplikacjakardiologiczna.model.database.Category
import com.github.aplikacjakardiologiczna.model.database.entity.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE id IN (:taskIds)")
    fun loadAllByIds(taskIds: IntArray): List<Task>

    @Query("SELECT * FROM task WHERE name LIKE :name")
    fun findByName(name: String): Task

    @Query("SELECT * FROM task WHERE category Like :cat")
    fun findByCategory(cat: Category): List<Task>

    @Insert
    fun insertAll(tasks: Task)

    @Delete
    fun delete(task: Task)
}
