package com.github.aplikacjakardiologiczna.heart

import android.util.Log
import com.github.aplikacjakardiologiczna.AppConstants
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.entity.UserInfo
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HeartPresenter(
        view: HeartContract.View,
        private val userTaskRepository: UserTaskRepository,
        private val uiContext: CoroutineContext = Dispatchers.Main
) : HeartContract.Presenter, CoroutineScope {

    private var view: HeartContract.View? = view
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun onCreateView() {
        setTasksPercent()
    }

    private fun setTasksPercent(): Job = launch {
        when (val result = userTaskRepository.getUserInfo()) {
            is Result.Success<UserInfo> -> onTaskDownloaded(result.data)
            is Result.Error -> Log.w("TASKS", "No tasks available.")
        }
    }

    private fun onTaskDownloaded(userInfo: UserInfo) {
        val completed = userInfo.userTasks.filter { it.time != null }.count()
        val percent = (completed.toFloat() / AppConstants.TASKS_PER_DAY * 100).toInt()

        view?.showProgressBar(percent)
    }

    override fun onDestroy() {
        this.view = null
    }
}
