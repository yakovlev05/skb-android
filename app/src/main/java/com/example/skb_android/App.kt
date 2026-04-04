package com.example.skb_android

import android.app.Application
import com.example.skb_android.di.dbModule
import com.example.skb_android.di.koinModule
import com.example.skb_android.di.networkModule
import com.example.skb_android.vacancy.di.vacanciesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(koinModule, networkModule, vacanciesModule, dbModule)
        }
    }
}
