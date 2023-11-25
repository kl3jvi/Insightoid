package com.kl3jvi.insightoid_api.di

import com.kl3jvi.insightoid_api.crashreporting.CrashReporter
import com.kl3jvi.insightoid_api.network.ApiClient
import com.kl3jvi.insightoid_api.network.ApiService
import com.kl3jvi.insightoid_api.storage.DataCache
import com.kl3jvi.insightoid_api.storage.LocalStorage
import org.koin.dsl.module
import retrofit2.Retrofit

val storageModule = module {
    single { LocalStorage() }
    single { DataCache() }
}

val crashReportingModule = module {
    single { CrashReporter() }
}

val networkModule = module {
    single<ApiService> {
        Retrofit.Builder()
            .baseUrl("https://insightoid.com")
            .build()
            .create(ApiService::class.java)
    }
    single { ApiClient(get()) }
}