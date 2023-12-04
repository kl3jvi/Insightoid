package com.kl3jvi.insightoid_api.di

import android.content.Context
import android.content.SharedPreferences
import com.kl3jvi.insightoid_api.storage.LocalStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val storageModule = module {
    // shared preferences
    single<SharedPreferences> {
        androidApplication().getSharedPreferences(
            "Insightoid_Preferences",
            Context.MODE_PRIVATE
        )
    }
    single { LocalStorage(get()) }
}