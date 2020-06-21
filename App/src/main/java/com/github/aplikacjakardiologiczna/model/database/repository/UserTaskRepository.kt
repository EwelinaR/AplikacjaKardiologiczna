package com.github.aplikacjakardiologiczna.model.database.repository

import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dao.UserTaskDao
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class UserTaskRepository private constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val userTaskDao: UserTaskDao
) {

    suspend fun getByDate(date: Date): Result<List<UserTask>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(userTaskDao.findByDate(date.time))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getTodayTasks(): Result<List<UserTask>> = withContext(ioDispatcher) {
        // dla testów użyta jest data jutrzejsza data, bo zadania będą dodawane na przyszły dzień
        val tomorrowDate: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            add(Calendar.DAY_OF_MONTH, 1)
        }
        return@withContext try {
            Result.Success(userTaskDao.findByDate(tomorrowDate.time.time))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun insertUserTask(tasksId: Array<Int>): Result<Unit> = withContext(ioDispatcher) {
        val userTasks: MutableList<UserTask> = ArrayList()

        val tomorrowDate: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            add(Calendar.DAY_OF_MONTH, 1)
        }
        for(id in tasksId)
            userTasks.add(UserTask(id, tomorrowDate.time))
        userTaskDao.insertAll(userTasks)

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