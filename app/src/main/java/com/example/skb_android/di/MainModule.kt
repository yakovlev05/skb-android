package com.example.skb_android.di

import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.navigation.NavHotRoute
import org.koin.dsl.module

val koinModule = module {
    single { MyBackStack(NavHotRoute) }
}
