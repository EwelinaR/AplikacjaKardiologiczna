package com.github.aplikacjakardiologiczna.main

import android.util.Log
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class MainPresenter(view: MainContract.View,
                    private val userTaskRepository: UserTaskRepository,
                    private val uiContext: CoroutineContext = Dispatchers.Main) : MainContract.Presenter, CoroutineScope {

    private var view: MainContract.View? = view
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun onViewCreated() {
        view?.showHeartView()
        insertUserTasksForTesting()
    }

    override fun onHeartTabClicked() {
        view?.showHeartView()
    }

    override fun onTasksTabClicked() {
        view?.showTasksView()
    }

    override fun onDestroy() {
        this.view = null
    }

    private fun insertUserTasksForTesting(): Job = launch {
        val userTask1 = UserTask(2, Calendar.getInstance().time, null)
        val userTask2 = UserTask(3, Calendar.getInstance().time, null)

        when (val result = userTaskRepository.insertUserTasks(listOf(userTask1, userTask2))) {
            is Result.Success<Unit> -> {
                Log.i("insert", result.data.toString())
            }
        }
    }
}
