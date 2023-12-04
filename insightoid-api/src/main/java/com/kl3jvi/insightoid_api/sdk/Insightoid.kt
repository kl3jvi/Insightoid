package com.kl3jvi.insightoid_api.sdk

//import com.kl3jvi.insightoid_api.crashreporting.CrashReporter
import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.kl3jvi.insightoid_api.crashreporting.CrashReporter
import com.kl3jvi.insightoid_api.crashreporting.ExceptionHandler
import com.kl3jvi.insightoid_api.di.listOfModules
import com.kl3jvi.insightoid_api.utils.LogTagProvider
import com.kl3jvi.insightoid_api.utils.info
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

object Insightoid : LogTagProvider {
    private var apiKey: String? = null
    private var enableCrashReporting: Boolean = false
    private var enableLogging: Boolean = false
    override val TAG: String = "Insightoid"

    fun getApiKey(): String? {
        requireNotNull(apiKey) { "Insightoid not initialized. Call Insightoid.Builder().initialize() first." }
        return apiKey
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
            info { "Context set" }
            return this
        }

        fun setApiKey(apiKey: String): Builder {
            this.apiKey = apiKey
            info { "API key set" }
            return this
        }

        fun setEnableCrashReporting(enable: Boolean): Builder {
            this.enableCrashReporting = enable
            info { "Crash reporting enabled" }
            return this
        }

        fun setEnableLogging(enable: Boolean): Builder {
            this.enableLogging = enable
            info { "Logging enabled" }
            return this
        }

        fun initialize() {
            requireNotNull(context) { "Context must be set" }
            require(apiKey.isNotEmpty()) { "API key must be set" }
            startKoin {
                androidContext(context!!)
                modules(listOfModules)
            }

            Insightoid.apiKey = this.apiKey
            Insightoid.enableCrashReporting = this.enableCrashReporting
            Insightoid.enableLogging = this.enableLogging

            context?.let {
                Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(it))
                initializeSendCrashData(it)
            }
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