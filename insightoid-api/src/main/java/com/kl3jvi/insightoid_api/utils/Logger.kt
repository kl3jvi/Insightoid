package com.kl3jvi.insightoid_api.utils

import android.util.Log
import com.kl3jvi.insightoid_api.config.InsightoidConfig
import org.koin.java.KoinJavaComponent.inject


@Suppress("PropertyName")
interface LogTagProvider {
    @Suppress("VariableNaming")
    val TAG: String
}

inline fun LogTagProvider.error(logMessage: () -> String) {
    if (isLoggingEnabled()) Log.e(TAG, logMessage.invoke())
}

inline fun LogTagProvider.error(error: Throwable?, logMessage: () -> String) {
    if (isLoggingEnabled()) {
        if (error == null) {
            Log.e(TAG, logMessage.invoke())
        } else {
            Log.e(TAG, logMessage.invoke(), error)
        }
    }
}

inline fun LogTagProvider.info(logMessage: () -> String) {
    if (isLoggingEnabled()) Log.i(TAG, logMessage.invoke())
}

inline fun LogTagProvider.verbose(logMessage: () -> String) {
    if (isLoggingEnabled()) Log.v(TAG, logMessage.invoke())
}

inline fun LogTagProvider.warn(logMessage: () -> String) {
    if (isLoggingEnabled()) Log.w(TAG, logMessage.invoke())
}


inline fun LogTagProvider.debug(logMessage: () -> String) {
    if (isLoggingEnabled()) Log.d(TAG, logMessage.invoke())
}


inline fun LogTagProvider.wtf(logMessage: () -> String) {
    if (isLoggingEnabled()) Log.wtf(TAG, logMessage.invoke())
}

fun isLoggingEnabled(): Boolean {
    val config: InsightoidConfig by inject(InsightoidConfig::class.java)
    return config.enableLogging
}
