package com.github.aplikacjakardiologiczna.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.github.aplikacjakardiologiczna.R

class CheckTaskNotificationReceiver : BroadcastReceiver() {

    companion object {
        private const val TASK_TAG = "TASK"
    }

    override fun onReceive(context: Context, intent: Intent) {
//        val extra = intent.getSerializableExtra(NotificationService.EXTRA_UNCOMPLETED_TASK, -1)
//        Log.i(TASK_TAG, "User completed task number $extra by clicking on notification button.")
//        Toast.makeText(context, R.string.completed_task_toast, Toast.LENGTH_SHORT).show()

        with(NotificationManagerCompat.from(context)) {
            cancel(NotificationService.NOTIFICATION_ID)
        }
    }
}
