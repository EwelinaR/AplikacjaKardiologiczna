package com.github.aplikacjakardiologiczna.model.database.repository

import android.util.Log
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dao.GroupDao
import com.github.aplikacjakardiologiczna.model.database.entity.Group
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroupRepository private constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val groupDao: GroupDao
) {

    suspend fun getAllGroups(): Result<List<Group>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(groupDao.getAll())
        } catch (e: Exception) {
            Log.e("error", "getAllGroups() failed", e)
            Result.Error(e)
        }
    }

    companion object {
        private var INSTANCE: GroupRepository? = null

        fun getInstance(groupDao: GroupDao): GroupRepository {
            return INSTANCE ?: GroupRepository(groupDao = groupDao)
                .apply { INSTANCE = this }
        }
    }

}
