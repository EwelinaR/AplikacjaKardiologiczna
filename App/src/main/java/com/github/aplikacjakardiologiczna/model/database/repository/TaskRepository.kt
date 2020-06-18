package com.github.aplikacjakardiologiczna.model.database.repository

import com.github.aplikacjakardiologiczna.model.database.dao.TaskDao
import com.github.aplikacjakardiologiczna.model.database.entity.Task
import com.github.aplikacjakardiologiczna.model.database.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TaskRepository private constructor(
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
        private val taskDao: TaskDao
) {

    suspend fun getAllTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(taskDao.getAll())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    companion object {
        private var INSTANCE: TaskRepository? = null

        fun getInstance(taskDao: TaskDao): TaskRepository {
            return INSTANCE ?: TaskRepository(taskDao = taskDao)
                    .apply { INSTANCE = this }
        }
    }

}
