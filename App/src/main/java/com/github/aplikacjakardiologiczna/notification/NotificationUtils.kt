package com.github.aplikacjakardiologiczna.notification

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import java.util.Calendar

class NotificationUtils(private var activity: Activity) {

    private lateinit var alarmManager: AlarmManager

    fun setAlarm() {
        Log.i("NOTIFICATION", "create notification")
        val alarmIntent = Intent(
                activity.applicationContext,
                AlarmNotificationReceiver::class.java
        )
        ContextCompat.startForegroundService(activity, alarmIntent)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 1)
        }

        alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val pendingIntent = createPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT)

        // for tests: run notification after a few seconds
        //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        //    SystemClock.elapsedRealtime() + 1000*10,
        //    10000,
        //    pendingIntent)
        // run notification everyday on scheduled time
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    private fun createPendingIntent(flagCancelCurrent: Int): PendingIntent? {
        return PendingIntent.getBroadcast(
                activity,
                10,
                Intent(activity.applicationContext, AlarmNotificationReceiver::class.java),
                flagCancelCurrent
        )
    }

    fun isAlarmUp(): Boolean {
        val alarmUp = createPendingIntent(PendingIntent.FLAG_NO_CREATE) != null

        if (alarmUp) {
            Log.i("ALARM", "Alarm is already active")
            return true
        }
        Log.i("ALARM", "Alarm is inactive")
        return false
    }

    fun cancelAlarm() {
        if (this::alarmManager.isInitialized) {
            alarmManager.cancel(createPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT))
            Log.i("ALARM", "deactivate alarm")
        }
    }
}
