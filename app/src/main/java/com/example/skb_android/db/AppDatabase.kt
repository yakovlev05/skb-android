package com.example.skb_android.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.skb_android.vacancy.data.dao.VacanciesDao
import com.example.skb_android.vacancy.data.entity.VacanciesDbEntity

@Database(entities = [VacanciesDbEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vacanciesDao(): VacanciesDao
}
