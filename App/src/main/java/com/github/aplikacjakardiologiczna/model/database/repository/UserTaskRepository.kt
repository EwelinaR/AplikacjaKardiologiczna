package com.github.aplikacjakardiologiczna.model.database.repository

import android.util.Log
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.entity.UserInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserTaskRepository(private val databaseManager: DatabaseManager) {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getUserInfo(): Result<UserInfo> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(databaseManager.getUserInfo())
        } catch (e: Exception) {
            Log.e("error", "getUserInfo() failed", e)
            Result.Error(e)
        }
    }

    suspend fun insertUserTasks(userInfo: UserInfo): Result<Unit> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(databaseManager.createTasks(userInfo))
            } catch (e: Exception) {
                Log.e("error", "insertUserTasks() failed", e)
                Result.Error(e)
            }
        }

    suspend fun completeUserTask(id: Int): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(databaseManager.markTaskAsCompleted(id))
        } catch (e: Exception) {
            Log.e("error", "updateUserTasks() failed", e)
            Result.Error(e)
        }
    }

    suspend fun getUserGroup(username: String): Result<String> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(databaseManager.getUserGroup(username))
        } catch (e: Exception) {
            Log.e("error", "getUserGroup() failed", e)
            Result.Error(e)
        }
    }
}
