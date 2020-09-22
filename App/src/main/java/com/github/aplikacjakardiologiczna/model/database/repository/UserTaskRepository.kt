package com.github.aplikacjakardiologiczna.model.database.repository

import android.util.Log
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dao.UserTaskDao
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.dynamodb.UserInfo
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserTaskRepository private constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val userTaskDao: UserTaskDao,   // will be removed
    private val databaseManager: DatabaseManager
) {

    suspend fun getUserInfo(): Result<UserInfo> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(databaseManager.getUserInfo())
        } catch (e: Exception) {
            Log.e("error", "getUserInfo() failed", e)
            Result.Error(e)
        }
    }

    suspend fun insertUserTasks(userTasks: List<UserTask>): Result<Unit> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(userTaskDao.insertAll(userTasks))
            } catch (e: Exception) {
                Log.e("error", "insertUserTasks() failed", e)
                Result.Error(e)
            }
        }

    suspend fun updateUserTask(id: Int): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(databaseManager.markTaskAsCompleted(id))
        } catch (e: Exception) {
            Log.e("error", "updateUserTasks() failed", e)
            Result.Error(e)
        }
    }

    // will be removed
    companion object {
        private var INSTANCE: UserTaskRepository? = null

        fun getInstance(userTaskDao: UserTaskDao, dynamoDB: DatabaseManager): UserTaskRepository {
            return INSTANCE ?: UserTaskRepository(
                userTaskDao = userTaskDao,
                databaseManager = dynamoDB
            )
                .apply { INSTANCE = this }
        }
    }
}
