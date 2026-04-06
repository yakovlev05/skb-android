package com.example.skb_android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.skb_android.di.dbModule
import com.example.skb_android.di.koinModule
import com.example.skb_android.di.networkModule
import com.example.skb_android.profile.di.profileModule
import com.example.skb_android.vacancy.di.vacanciesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(koinModule, networkModule, vacanciesModule, dbModule, profileModule)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            getString(R.string.notifications_channel_id),
            getString(R.string.notifications_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
