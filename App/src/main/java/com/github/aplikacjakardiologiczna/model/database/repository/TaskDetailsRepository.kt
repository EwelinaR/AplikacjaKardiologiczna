package com.github.aplikacjakardiologiczna.model.database.repository

import android.util.Log
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.entity.TaskDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskDetailsRepository(private val databaseManager: DatabaseManager) {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getTasksDetails(group: String, ids: List<Int>): Result<List<TaskDetails>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(databaseManager.getTasksDetails(group, ids))
            } catch (e: Exception) {
                Log.e("error", "getTasksDetails() failed", e)
                Result.Error(e)
            }
        }

    suspend fun getTasksFromGroup(group: String): Result<List<Int>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(databaseManager.getTaskIdsFromGroup(group))
            } catch (e: Exception) {
                Log.e("error", "getTasksFromGroup() failed", e)
                Result.Error(e)
            }
        }
}
