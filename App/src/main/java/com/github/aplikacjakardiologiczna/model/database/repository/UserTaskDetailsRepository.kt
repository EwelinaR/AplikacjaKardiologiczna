package com.github.aplikacjakardiologiczna.model.database.repository

import android.util.Log
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dao.UserTaskDetailsDao
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.dynamodb.TaskDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserTaskDetailsRepository private constructor(
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
        private val userTaskDetailsDao: UserTaskDetailsDao,
        private val databaseManager: DatabaseManager
) {


    suspend fun getTaskDescriptions(group: String, ids: List<Int>): Result<List<TaskDetails>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(databaseManager.getTaskDescription(group, ids))
        } catch (e: Exception) {
            Log.e("error", "getTaskDescriptions() failed", e)
            Result.Error(e)
        }
    }

    companion object {
        private var INSTANCE: UserTaskDetailsRepository? = null

        fun getInstance(userTaskDetailsDao: UserTaskDetailsDao, databaseManager: DatabaseManager): UserTaskDetailsRepository {
            return INSTANCE ?: UserTaskDetailsRepository(
                userTaskDetailsDao = userTaskDetailsDao,
                databaseManager = databaseManager)
                    .apply { INSTANCE = this }
        }
    }
}
