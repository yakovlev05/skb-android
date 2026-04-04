package com.example.skb_android.di

import androidx.room.Room
import com.example.skb_android.db.AppDatabase
import org.koin.dsl.module

val dbModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    single { get<AppDatabase>().vacanciesDao() }
}
