package com.github.aplikacjakardiologiczna.model.database.repository

import android.util.Log
import com.github.aplikacjakardiologiczna.extensions.CalendarExtensions.atStartOfDay
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dao.UserTaskDao
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date


class UserTaskRepository private constructor(
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
        private val userTaskDao: UserTaskDao
) {

    suspend fun insertUserTasks(userTasks: List<UserTask>): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(userTaskDao.insertAll(userTasks))
        } catch (e: Exception) {
            Log.e("error", "insertUserTasks() failed", e)
            Result.Error(e)
        }
    }

    suspend fun getUserTasks(date: Date): Result<List<UserTask>> = withContext(ioDispatcher) {
        val dateStart = Calendar.getInstance().atStartOfDay(date)

        return@withContext try {
            Result.Success(userTaskDao.findByDate(dateStart))
        } catch (e: Exception) {
            Log.e("error", "getUserTasks() failed", e)
            Result.Error(e)
        }
    }

    suspend fun countUserTasks(date: Date): Result<Int> = withContext(ioDispatcher) {
        val dateStart = Calendar.getInstance().atStartOfDay(date)

        return@withContext try {
            Result.Success(userTaskDao.countTasks(dateStart))
        } catch (e: Exception) {
            Log.e("error", "countUserTasks() failed", e)
            Result.Error(e)
        }
    }

    suspend fun updateUserTask(userTask: UserTask): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(userTaskDao.update(userTask))
        } catch (e: Exception) {
            Log.e("error", "updateUserTasks() failed", e)
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
