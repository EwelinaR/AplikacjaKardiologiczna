package com.github.aplikacjakardiologiczna.model.database

import android.content.Context
import com.github.aplikacjakardiologiczna.extensions.CalendarExtensions.atStartOfDay
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import com.github.aplikacjakardiologiczna.model.database.repository.TaskRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Random
import kotlin.coroutines.CoroutineContext


object TaskInserter: CoroutineScope {

    private const val numberOfTasksPerDay = 4
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun insertUserTasksForToday(context: Context): Job = launch {
        val todayDate: Calendar = Calendar.getInstance()
        val todayDateStart = Calendar.getInstance().atStartOfDay(todayDate.time)

        insertUserTasks(context, todayDateStart)
    }

    fun insertUserTasksForTomorrow(context: Context): Job = launch {
        val tomorrowDate: Calendar = Calendar.getInstance()
        tomorrowDate.add(Calendar.DAY_OF_MONTH, 1)
        val tomorrowDateStart = Calendar.getInstance().atStartOfDay(tomorrowDate.time)

        insertUserTasks(context, tomorrowDateStart)
    }

    private fun insertUserTasks(context: Context, date: Date): Job = launch {
        val db = AppDatabase.getInstance(context)
        val userTaskRepository = UserTaskRepository.getInstance(db.userTaskDao())
        val taskRepository = TaskRepository.getInstance(db.taskDao())

        if(existUserTask(userTaskRepository, date) == false){
            val ids = getIds(taskRepository)
            if(ids != null){
                val tasks = randomTasks(ids, date)
                insert(userTaskRepository, tasks)
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

    private suspend fun existUserTask(userTaskRepository: UserTaskRepository, date: Date): Boolean?{
        when (val result = userTaskRepository.countUserTasks(date)) {
            is Result.Success<Int> -> {
                if(result.data > 0)
                    return true
                return false
            }
        }
        return null
    }

    private suspend fun getIds(taskRepository: TaskRepository): List<Int>? {
        when (val result = taskRepository.getAllIds()) {
            is Result.Success<List<Int>> -> {
                return result.data
            }
        }
        return null
    }

    private suspend fun insert(userTaskRepository: UserTaskRepository, tasks: List<UserTask>): Boolean{
        when (val result = userTaskRepository.insertUserTasks(tasks)) {
            is Result.Success<Unit> -> {
                return true
            }
        }
        return false
    }
}
