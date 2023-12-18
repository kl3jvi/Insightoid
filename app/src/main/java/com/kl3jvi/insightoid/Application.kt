package com.kl3jvi.insightoid

import android.app.Application
import com.kl3jvi.insightoid_api.sdk.Insightoid

class InsightoidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Insightoid.Builder()
            .withContext(this)
            .setApiKey("ea4d90e3-c056-406e-ba5b-79aa92da95ba")
            .setEnableCrashReporting(true)
            .setEnableLogging(true)
            .initialize()
    }
}