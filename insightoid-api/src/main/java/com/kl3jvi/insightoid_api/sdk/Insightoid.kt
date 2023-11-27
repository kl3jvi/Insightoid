package com.kl3jvi.insightoid_api.sdk

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.kl3jvi.insightoid_api.config.Configuration
import com.kl3jvi.insightoid_api.crashreporting.CrashReporter
import com.kl3jvi.insightoid_api.crashreporting.ExceptionHandler
import org.koin.core.KoinApplication
import java.util.concurrent.TimeUnit

/**
 * This is the main object for the Insightoid SDK.
 * It provides the main entry point for the SDK functionality.
 */
object Insightoid {
    private val koinApplication = KoinApplication.init()

    /**
     * This function initializes the Insightoid SDK.
     * It should be called before any other SDK functions are used.
     */
    fun initialize(
        context: Context,
        apiKey: String = "",
        configuration: Configuration? = null,
    ) {
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(context))
        koinApplication.modules(listOf())
        initializeSendCrashData(context)
    }

    private fun initializeSendCrashData(context: Context) {
        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val sendCrashDataWorkRequest =
            PeriodicWorkRequest.Builder(
                CrashReporter::class.java,
                15,
                TimeUnit.MINUTES,
            ).setConstraints(constraints).build()

        WorkManager.getInstance(context).enqueue(sendCrashDataWorkRequest)
    }

    /**
     * This class holds the configuration options for the Insightoid SDK.
     */
}
