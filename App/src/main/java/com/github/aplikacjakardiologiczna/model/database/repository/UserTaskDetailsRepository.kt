package com.github.aplikacjakardiologiczna.model.database.repository

import android.util.Log
import com.github.aplikacjakardiologiczna.extensions.CalendarExtensions.atEndOfDay
import com.github.aplikacjakardiologiczna.extensions.CalendarExtensions.atStartOfDay
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dao.UserTaskDetailsDao
import com.github.aplikacjakardiologiczna.model.database.entity.UserTaskDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class UserTaskDetailsRepository private constructor(
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
        private val userTaskDetailsDao: UserTaskDetailsDao
) {

    suspend fun getTasksForToday(): Result<List<UserTaskDetails>> = withContext(ioDispatcher) {
        val todaysDate: Calendar = Calendar.getInstance()

        val todaysDateStart = Calendar.getInstance().atStartOfDay(todaysDate.time)
        val todaysDateEnd = Calendar.getInstance().atEndOfDay(todaysDate.time)

        return@withContext try {
            Result.Success(userTaskDetailsDao.getAllInDate(todaysDateStart, todaysDateEnd))
        } catch (e: Exception) {
            Log.e("error", "getTasksForToday() failed", e)
            Result.Error(e)
        }
    }

    companion object {
        private var INSTANCE: UserTaskDetailsRepository? = null

        fun getInstance(userTaskDetailsDao: UserTaskDetailsDao): UserTaskDetailsRepository {
            return INSTANCE ?: UserTaskDetailsRepository(userTaskDetailsDao = userTaskDetailsDao)
                    .apply { INSTANCE = this }
        }
    }
}
