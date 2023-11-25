package com.kl3jvi.insightoid_api.di

import com.kl3jvi.insightoid_api.network.ApiClient
import com.kl3jvi.insightoid_api.network.ApiService
import com.kl3jvi.insightoid_api.storage.DataCache
import com.kl3jvi.insightoid_api.storage.LocalStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit

val storageModule =
    module {
        single { LocalStorage(androidApplication().applicationContext) }
        single { DataCache() }
    }

val networkModule =
    module {
        single<ApiService> {
            Retrofit.Builder()
                .baseUrl("https://insightoid.com")
                .build()
                .create(ApiService::class.java)
        }
        single { ApiClient(get()) }
    }

val listOfModules = listOf(storageModule, networkModule)