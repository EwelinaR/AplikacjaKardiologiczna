package com.github.aplikacjakardiologiczna.main

import android.util.Log
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import com.github.aplikacjakardiologiczna.model.database.repository.TaskRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import com.github.aplikacjakardiologiczna.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Random
import kotlin.coroutines.CoroutineContext

class MainPresenter(view: MainContract.View,
                    private val userTaskRepository: UserTaskRepository,
                    private val taskRepository: TaskRepository,
                    private val uiContext: CoroutineContext = Dispatchers.Main) : MainContract.Presenter, CoroutineScope {

    private var view: MainContract.View? = view
    private var job: Job = Job()
    private val numberOfTasksPerDay = 4

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun onViewCreated() {
        view?.showHeartView()
        insertUserTasks()
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

    private fun randomTasks(ids: List<Int>, date: Date): List<UserTask>{
        val rand = Random()
        var randInt: Int
        val randomIds = IntArray(numberOfTasksPerDay)
        randomIds[0] = rand.nextInt(ids.size)
        for (i in 1 until randomIds.size){
            randInt = rand.nextInt(ids.size)
            while(randInt in randomIds) randInt = rand.nextInt(ids.size)
            randomIds[i] = randInt
        }

        val userTasks = Array(numberOfTasksPerDay) {i ->
            UserTask(ids[randomIds[i]], date)
        }
        return userTasks.toList()
    }

    private suspend fun existUserTask(date: Date): Boolean?{
        when (val result = userTaskRepository.countUserTasks(date)) {
            is Result.Success<Int> -> {
                if(result.data > 0)
                    return true
                return false
            }
        }
        return null
    }

    private suspend fun getIds(): List<Int>? {
        when (val result = taskRepository.getAllIds()) {
            is Result.Success<List<Int>> -> {
                return result.data
            }
        }
        return null
    }

    private suspend fun insert(tasks: List<UserTask>): Boolean{
        when (val result = userTaskRepository.insertUserTasks(tasks)) {
            is Result.Success<Unit> -> {
                Log.i("insert", result.data.toString())
                return true
            }
        }
        return false
    }

    private fun insertUserTasks(): Job = launch {
        val todaysDate: Calendar = Calendar.getInstance()
        val todaysDateStart = DateUtils.atStartOfDay(todaysDate.time)

        if(existUserTask(todaysDateStart) == false){
            val ids = getIds()
            if(ids != null){
                val tasks = randomTasks(ids, todaysDateStart)
                insert(tasks)
            }
        }
    }
}
