package com.kl3jvi.insightoid_api.sdk

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.kl3jvi.insightoid_api.crashreporting.CrashReporter
import com.kl3jvi.insightoid_api.crashreporting.ExceptionHandler
import org.koin.core.KoinApplication
import java.util.concurrent.TimeUnit

object Insightoid {
    private val koinApplication = KoinApplication.init()
    private var apiKey: String? = null
    private var enableCrashReporting: Boolean = false
    private var enableLogging: Boolean = false

    fun getApiKey(): String? {
        requireNotNull(apiKey) { "Insightoid not initialized. Call Insightoid.Builder().initialize() first." }
        return apiKey
    }

    fun isCrashReportingEnabled(): Boolean {
        requireNotNull(apiKey) { "Insightoid not initialized. Call Insightoid.Builder().initialize() first." }
        return enableCrashReporting
    }

    fun isLoggingEnabled(): Boolean {
        requireNotNull(apiKey) { "Insightoid not initialized. Call Insightoid.Builder().initialize() first." }
        return enableLogging
    }

    class Builder {
        private var context: Context? = null
        private var apiKey: String = ""
        private var enableCrashReporting: Boolean = false
        private var enableLogging: Boolean = false

        fun withContext(context: Context): Builder {
            this.context = context
            return this
        }

        fun setApiKey(apiKey: String): Builder {
            this.apiKey = apiKey
            return this
        }

        fun setEnableCrashReporting(enable: Boolean): Builder {
            this.enableCrashReporting = enable
            return this
        }

        fun setEnableLogging(enable: Boolean): Builder {
            this.enableLogging = enable
            return this
        }

        fun initialize() {
            requireNotNull(context) { "Context must be set" }
            require(apiKey.isNotEmpty()) { "API key must be set" }

            context?.let {
                Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(it))
                koinApplication.modules(listOf())
                initializeSendCrashData(it)
            }

            Insightoid.apiKey = this.apiKey
            Insightoid.enableCrashReporting = this.enableCrashReporting
            Insightoid.enableLogging = this.enableLogging
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
    }
}