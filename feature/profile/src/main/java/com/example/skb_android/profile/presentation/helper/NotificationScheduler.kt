package com.example.skb_android.profile.presentation.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.example.skb_android.profile.presentation.receiver.NotificationsReceiver
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class NotificationScheduler(
    private val context: Context
) {

    fun saveNotification(time: LocalTime, text: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val dateTime = LocalDateTime.of(LocalDate.now(), time)


        val timeInMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val notifyIntent = Intent(context, NotificationsReceiver::class.java)
        notifyIntent.putExtras(
            Bundle().apply {
                putString(ProfileConsts.NOTIFICATION, text)
                putInt(ProfileConsts.NOTIFICATION_ID, timeInMillis.hashCode())
            }
        )

        val notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            timeInMillis.hashCode(),
            notifyIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                Log.w(TAG, "AlarmManager cannot schedule")
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    notifyPendingIntent
                )
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    notifyPendingIntent
                )
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to set alarm: ${e.message}")
        }

    }

    companion object {
        const val TAG = "NotificationScheduler"
    }

}