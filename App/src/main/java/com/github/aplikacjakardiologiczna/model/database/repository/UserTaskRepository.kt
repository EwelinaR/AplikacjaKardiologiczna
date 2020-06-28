package com.github.aplikacjakardiologiczna.model.database.repository

import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dao.UserTaskDao
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserTaskRepository private constructor(
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
        private val userTaskDao: UserTaskDao
) {

    suspend fun insertUserTasks(userTasks: List<UserTask>): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(userTaskDao.insertAll(userTasks))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    companion object {
        private var INSTANCE: UserTaskRepository? = null

        fun getInstance(userTaskDao: UserTaskDao): UserTaskRepository {
            return INSTANCE ?: UserTaskRepository(userTaskDao = userTaskDao)
                    .apply { INSTANCE = this }
        }
    }
}
