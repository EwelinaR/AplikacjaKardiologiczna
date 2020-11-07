package com.github.aplikacjakardiologiczna.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.main.MainActivity
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class CheckTaskNotificationReceiver : BroadcastReceiver(), CoroutineScope {

    companion object {
        private const val TAG = "CHECK_TASK_RECEIVER"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + Job()

    override fun onReceive(context: Context, intent: Intent) {
        intent.extras?.let {
            if (it.containsKey(NotificationService.EXTRA_UNCOMPLETED_TASK)) {
                val uncompletedTask =
                    intent.getSerializableExtra(NotificationService.EXTRA_UNCOMPLETED_TASK) as UserTask
                completeTask(context, uncompletedTask)
            }

            with(NotificationManagerCompat.from(context)) {
                cancel(NotificationService.NOTIFICATION_ID)
            }
        }
    }

    private fun completeTask(context: Context, userTask: UserTask) {
        val userTaskRepository = UserTaskRepository(DatabaseManager(context))

        launch {
            if (userTaskRepository.completeUserTask(userTask.index) is Result.Success) {
                withContext(Dispatchers.Main) {
                    Log.i(TAG, "User completed task with index ${userTask.index} by clicking on notification button.")
                    Toast.makeText(context, R.string.completed_task_toast, Toast.LENGTH_SHORT)
                        .show()

                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
                        putExtra(MainActivity.EXTRA_SHOW_TASKS, true)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

}
