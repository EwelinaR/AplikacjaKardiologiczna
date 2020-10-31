package com.github.aplikacjakardiologiczna.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.model.database.entity.TaskDetails
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class CheckTaskNotificationReceiver : BroadcastReceiver(), CoroutineScope {

    companion object {
        private const val TASK_TAG = "TASK"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + Job()

    override fun onReceive(context: Context, intent: Intent) {
        intent.extras?.let {
            if (it.containsKey(NotificationService.EXTRA_UNCOMPLETED_TASK)) {
                val uncompletedTask =
                    intent.getSerializableExtra(NotificationService.EXTRA_UNCOMPLETED_TASK) as UserTask
                Log.i(TASK_TAG, "User completed task by clicking on notification button.")
                Toast.makeText(context, R.string.completed_task_toast, Toast.LENGTH_SHORT).show()

                uncompletedTask.time = Calendar.getInstance().time.toString()

                /*
                        val task = userInfo.userTasks[position]
        task.let {
            task.time = if (isChecked) Calendar.getInstance().time.toString() else null
            updateUserTask(task, itemView)
        }
                 */
            }
        }

        with(NotificationManagerCompat.from(context)) {
            cancel(NotificationService.NOTIFICATION_ID)
        }
    }
}
