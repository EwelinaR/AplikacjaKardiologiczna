package com.github.aplikacjakardiologiczna.notification

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import java.util.Calendar

class NotificationUtils(private val activity: Activity) {

    companion object {
        private const val NOTIFICATION_TAG = "NOTIFICATION"
        private const val ALARM_TAG = "ALARM"
        private const val HOUR_OF_NOTIFICATION = 21
        private const val MINUTE_OF_NOTIFICATION = 1
        private const val PENDING_INTENT_REQUEST_CODE = 10
    }

    private lateinit var alarmManager: AlarmManager

    fun setAlarm() {
        Log.i(NOTIFICATION_TAG, "Create notification.")
        val alarmIntent = Intent(
            activity.applicationContext,
            AlarmNotificationReceiver::class.java
        )
        ContextCompat.startForegroundService(activity, alarmIntent)

        val notificationTime: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, HOUR_OF_NOTIFICATION)
            set(Calendar.MINUTE, MINUTE_OF_NOTIFICATION)
        }

        alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val pendingIntent = createPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT)

        // for tests: run notification after a few seconds
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            SystemClock.elapsedRealtime() + 1000*10,
//            10000,
//            pendingIntent)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, notificationTime.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }

    private fun createPendingIntent(flagCancelCurrent: Int): PendingIntent? {
        return PendingIntent.getBroadcast(
            activity,
            PENDING_INTENT_REQUEST_CODE,
            Intent(activity.applicationContext, AlarmNotificationReceiver::class.java),
            flagCancelCurrent
        )
    }

    fun isAlarmUp(): Boolean = (createPendingIntent(PendingIntent.FLAG_NO_CREATE) != null).also {
        Log.i(ALARM_TAG, "Is alarm up: $it.")
    }

    fun cancelAlarm(){
        if(this::alarmManager.isInitialized){
            alarmManager.cancel(createPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT))
            Log.i(ALARM_TAG, "Alarm cancelled.")
        }
    }
}
