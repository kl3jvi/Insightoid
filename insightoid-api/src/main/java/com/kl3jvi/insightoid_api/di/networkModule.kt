package com.kl3jvi.insightoid_api.di

import android.content.Context
import android.content.SharedPreferences
import com.kl3jvi.insightoid_api.network.ApiClient
import com.kl3jvi.insightoid_api.network.ApiService
import com.kl3jvi.insightoid_api.storage.DataCache
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit

val storageModule = module {
    // shared preferences
    single<SharedPreferences> {
        androidApplication().getSharedPreferences(
            "CrashData",
            Context.MODE_PRIVATE
        )
    }
//    single { LocalStorage() }
    single { DataCache() }
}

val networkModule =
    module {
        single<ApiService> {
            Retrofit.Builder()
                .baseUrl("https://insightoid.io/api/")
                .build()
                .create(ApiService::class.java)
        }
        single { ApiClient(get()) }
    }

val listOfModules = listOf(storageModule, networkModule)
