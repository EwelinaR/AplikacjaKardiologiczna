package com.github.aplikacjakardiologiczna.model.database.repository

import androidx.core.graphics.scaleMatrix
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dao.UserTaskDao
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*


class UserTaskRepository private constructor(
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
        private val userTaskDao: UserTaskDao
) {

    suspend fun getTasksForToday(): Result<List<UserTask>> = withContext(ioDispatcher) {
        val todaysDate: Calendar = Calendar.getInstance()
        val todaysDateStart = atStartOfDay(todaysDate.time)
        val todaysDateEnd = atEndOfDay(todaysDate.time)

        return@withContext try {
            Result.Success(userTaskDao.loadAllInDate(todaysDateStart, todaysDateEnd))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

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

    private fun atEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999
        return calendar.time
    }

    private fun atStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }
}
