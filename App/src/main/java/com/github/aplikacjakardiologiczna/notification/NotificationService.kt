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
import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.main.MainActivity
import com.github.aplikacjakardiologiczna.model.database.UserTaskInitializer
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask
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
                    intent.getSerializableExtra(EXTRA_UNCOMPLETED_TASK) as UserTask
                showNotification(uncompletedTask)
            }
        }
    }

    private fun initializeUserTasksForTomorrow() {
        val dynamoDb = DatabaseManager(this)

        val taskInitializer = UserTaskInitializer(
            TaskDetailsRepository(dynamoDb),
            UserTaskRepository(dynamoDb),
            AppSettings(this),
            ::initializeTasksCallback
        )
        taskInitializer.initializeUserTasks(false)
    }

    private fun initializeTasksCallback(wasSuccessful: Boolean) {
        // Do nothing
    }

    private fun showNotification(uncompletedUserTask: UserTask) {
        createChannel()
        val notification = buildNotification(uncompletedUserTask)
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

    private fun buildNotification(uncompletedUserTask: UserTask): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
            putExtra(MainActivity.EXTRA_SHOW_TASKS, true)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val buttonIntent = Intent(this, CheckTaskNotificationReceiver::class.java).apply {
            putExtra(EXTRA_UNCOMPLETED_TASK, uncompletedUserTask)
        }
        val buttonPendingIntent =
            PendingIntent.getBroadcast(this, 0, buttonIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(
            this, CHANNEL_ID
        ).apply {
            setSmallIcon(R.drawable.ic_notification)
            setContentIntent(pendingIntent)
            setContentTitle(getString(R.string.task_notify_title))
            setContentText(uncompletedUserTask.taskDetails?.description)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_DEFAULT
            addAction(
                R.drawable.ic_notification, getString(R.string.notify_button),
                buttonPendingIntent
            )
        }.build()
    }
}
