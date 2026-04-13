package com.example.skb_android.profile.presentation.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.skb_android.profile.R
import com.example.skb_android.profile.presentation.helper.ProfileConsts
import androidx.core.net.toUri

class NotificationsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val text = intent.getStringExtra(ProfileConsts.NOTIFICATION) ?: return
        val notificationId = intent.getIntExtra(ProfileConsts.NOTIFICATION_ID, text.hashCode())

        val activityIntent = Intent(Intent.ACTION_VIEW, "skb_example_app://main".toUri())

        val builder = NotificationCompat.Builder(
            context,
            context.getString(com.example.skb_android.core.R.string.notifications_channel_id)
        )

        builder
            .setSmallIcon(R.drawable.material_icon_clock)
            .setContentTitle("Пришло время...")
            .setContentText(text)
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    activityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )

        notificationManager.notify(notificationId, builder.build())
    }

}
