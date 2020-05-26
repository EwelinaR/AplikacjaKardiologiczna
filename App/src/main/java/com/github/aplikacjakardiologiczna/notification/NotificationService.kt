package com.github.aplikacjakardiologiczna.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.main.MainActivity

class NotificationService : IntentService("NotificationService") {

    private lateinit var notification: Notification

    companion object {
        const val NOTIFICATION_ID = 112
        private const val CHANNEL_ID = "aplikacjakardiologiczna_notification_tasks"
        private const val NOTIFY_TITLE = "Masz niewykonane zadania na dzisiaj"
        private const val NOTIFY_TEXT = "Wykonaj zadania aby prowadzić zdrowy tryb życia."
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
        Log.i("NOTIFICATION", "Showing new notification")
        createChannel()
        buildNotification()

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, notification)
        }
    }

    private fun buildNotification(){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val buttonIntent = Intent(this, CheckTaskNotificationReceiver::class.java).apply {
            action = "Check task as done"
            putExtra("task_id","1")
        }
        val buttonPendingIntent = PendingIntent.getBroadcast(this,0,buttonIntent,0)



        val builder = NotificationCompat.Builder(this, CHANNEL_ID
            ).apply {
                setSmallIcon(R.drawable.notify_heart)
                setContentIntent(pendingIntent)
                setContentTitle(NOTIFY_TITLE)
                setContentText(NOTIFY_TEXT)
                setAutoCancel(true)
                setPriority(NotificationCompat.PRIORITY_DEFAULT)
                addAction(R.drawable.notify_heart, "zrobione!",
                        buttonPendingIntent)
        }
        notification = builder.build()
        startForeground(1, notification)
    }
}