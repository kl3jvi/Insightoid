package com.kl3jvi.insightoid_api.sdk

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.kl3jvi.insightoid_api.config.InsightoidConfig
import com.kl3jvi.insightoid_api.crashreporting.CrashReporter
import com.kl3jvi.insightoid_api.crashreporting.ExceptionHandler
import com.kl3jvi.insightoid_api.di.listOfModules
import com.kl3jvi.insightoid_api.utils.LogTagProvider
import com.kl3jvi.insightoid_api.utils.info
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

object Insightoid : LogTagProvider {
    override val TAG: String = "Insightoid"
    class Builder : LogTagProvider {
        private var context: Context? = null
        private var apiKey: String = ""
        private var enableCrashReporting: Boolean = false
        private var enableLogging: Boolean = false
        override val TAG: String get() = "Insightoid"

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

            val config = InsightoidConfig(apiKey, enableCrashReporting, enableLogging)

            startKoin {
                androidContext(context!!)
                modules(
                    module {
                        single { config }
                    } + listOfModules
                )
            }

            Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(context!!))
            initializeSendCrashData(context!!)
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