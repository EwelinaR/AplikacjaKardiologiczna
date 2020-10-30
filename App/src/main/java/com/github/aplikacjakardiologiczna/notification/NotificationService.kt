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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.main.MainActivity
import com.github.aplikacjakardiologiczna.model.database.UserTaskInitializer
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.entity.TaskDetails
import com.github.aplikacjakardiologiczna.model.database.repository.TaskDetailsRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository

class NotificationService : IntentService(NOTIFICATION_SERVICE_NAME) {

    companion object {
        const val NOTIFICATION_ID = 112
        private const val CHANNEL_ID = "aplikacjakardiologiczna_notification_tasks"
        private const val NOTIFICATION_SERVICE_NAME = "NotificationService"
        const val EXTRA_UNCOMPLETED_TASK = "UNCOMPLETED_TASK"
    }

    override fun onHandleIntent(intent: Intent?) {
        initializeUserTasksForTomorrow()

        intent?.extras?.let {
            if (it.containsKey(EXTRA_UNCOMPLETED_TASK)) {
                val uncompletedTask =
                    intent.getSerializableExtra(EXTRA_UNCOMPLETED_TASK) as TaskDetails
                showNotification(uncompletedTask)
            }
        }
    }

    private fun initializeUserTasksForTomorrow() {
        val dynamoDb = DatabaseManager(this)

        val taskInitializer = UserTaskInitializer(
            TaskDetailsRepository(dynamoDb),
            UserTaskRepository(dynamoDb),
            ::initializeTasksCallback
        )
        taskInitializer.initializeUserTasks(false)
    }

    private fun initializeTasksCallback(wasSuccessful: Boolean) {
        // TODO Do sth
    }

    private fun showNotification(taskDetails: TaskDetails) {
        createChannel()
        val notification = buildNotification(taskDetails)
        startForeground(1, notification)
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = getString(R.string.notification_description)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(taskDetails: TaskDetails): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val buttonIntent = Intent(this, CheckTaskNotificationReceiver::class.java).apply {
            putExtra(EXTRA_UNCOMPLETED_TASK, taskDetails)
        }
        val buttonPendingIntent = PendingIntent.getBroadcast(this, 0, buttonIntent, 0)

        return NotificationCompat.Builder(
            this, CHANNEL_ID
        ).apply {
            setSmallIcon(R.drawable.ic_notification)
            setContentIntent(pendingIntent)
            setContentTitle(getString(R.string.task_notify_title))
            setContentText(taskDetails.description)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_DEFAULT
            addAction(
                R.drawable.ic_notification, getString(R.string.notify_button),
                buttonPendingIntent
            )
        }.build()
    }
}
