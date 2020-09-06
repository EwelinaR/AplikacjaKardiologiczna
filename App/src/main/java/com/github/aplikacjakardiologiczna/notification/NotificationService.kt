package com.github.aplikacjakardiologiczna.notification

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.main.MainActivity
import com.github.aplikacjakardiologiczna.model.database.AppDatabase
import com.github.aplikacjakardiologiczna.model.database.UserTaskInitializer
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.repository.TaskRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository

class NotificationService : IntentService("NotificationService") {

    private lateinit var notification: Notification

    companion object {
        const val NOTIFICATION_ID = 112
        private const val CHANNEL_ID = "aplikacjakardiologiczna_notification_tasks"
        private const val CHANNEL_NAME = "reminder"
        private const val CHANNEL_DESCRIPTION = "Reminder about tasks"
    }

    private fun createChannel() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.description =
                    CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        initializeUserTasksForTomorrow()
        showNotification()
    }

    private fun initializeUserTasksForTomorrow() {
        val db = AppDatabase.getInstance(this)
        val dynamoDb = DatabaseManager(this)

        val taskInitializer = UserTaskInitializer(
            TaskRepository.getInstance(db.taskDao()),
            UserTaskRepository.getInstance(db.userTaskDao(), dynamoDb),
            ::initializeTasksCallback
        )
        taskInitializer.initializeUserTasks(false)
    }

    private fun initializeTasksCallback(wasSuccessful: Boolean) {
        // TODO Do sth
    }

    private fun showNotification() {
        Log.i("NOTIFICATION", "Showing new notification")
        createChannel()
        buildNotification()
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, notification)
        }
    }

    private fun buildNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val taskId = "task_id"

        // TODO connect with DB and get not completed task
        val activityId = 33
        val activityDescription = "-> Zrób 10 przysiadów <-"

        val buttonIntent = Intent(this, CheckTaskNotificationReceiver::class.java).apply {
            putExtra(taskId, activityId.toString())
        }
        val buttonPendingIntent = PendingIntent.getBroadcast(this, 0, buttonIntent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID
        ).apply {
            setSmallIcon(R.drawable.notify_heart)
            setContentIntent(pendingIntent)
            setContentTitle(getString(R.string.task_notify_title))
            setContentText(activityDescription)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_DEFAULT
            addAction(R.drawable.notify_heart, getString(R.string.notify_button),
                buttonPendingIntent)
        }
        notification = builder.build()
        startForeground(1, notification)
    }
}
