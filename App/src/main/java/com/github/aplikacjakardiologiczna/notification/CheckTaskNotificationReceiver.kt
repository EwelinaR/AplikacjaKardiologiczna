package com.github.aplikacjakardiologiczna.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.github.aplikacjakardiologiczna.R

class CheckTaskNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val extra = intent.getStringExtra("task_id")
        Log.i("TASK", "User finish task number $extra by notification button")
        Toast.makeText(context, R.string.completed_task_toast, Toast.LENGTH_SHORT).show()

        with(NotificationManagerCompat.from(context)) {
            cancel(NotificationService.NOTIFICATION_ID)
        }
    }
}