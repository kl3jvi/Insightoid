package com.kl3jvi.insightoid

import android.app.Application
import com.kl3jvi.insightoid_api.sdk.Insightoid

class InsightoidApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        Insightoid.Builder()
            .withContext(this)
            .setApiKey("696b7f50-7ec2-4f67-89dc-c6922a2a087d")
            .setEnableCrashReporting(true)
            .setEnableLogging(true)
            .initialize()
    }
}