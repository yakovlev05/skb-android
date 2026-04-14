package com.example.skb_android.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.skb_android.core.util.API_HH
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(get()))
            .build()
    }

    single {
        val json = Json {
            ignoreUnknownKeys = true
        }

        Retrofit.Builder()
            .baseUrl(API_HH)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .client(get())
            .build()
    }
}