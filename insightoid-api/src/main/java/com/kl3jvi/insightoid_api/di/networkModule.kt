package com.kl3jvi.insightoid_api.di

import com.kl3jvi.insightoid_api.network.ApiClient
import com.kl3jvi.insightoid_api.network.ApiService
import com.kl3jvi.insightoid_api.sdk.Insightoid
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("projectId", "${Insightoid.getApiKey()}")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    single<ApiService> {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.55:8080/api/") // localhost:8080
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    single { ApiClient(get()) }
}

val listOfModules = listOf(storageModule, networkModule)
