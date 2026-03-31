package com.example.skb_android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.navigation.NavHotRoute
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val koinModule = module {
    single { MyBackStack(NavHotRoute) }

    single { getDataStore(androidContext()) }
}

fun getDataStore(androidContext: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create {
        androidContext.preferencesDataStoreFile("default")
    }
}