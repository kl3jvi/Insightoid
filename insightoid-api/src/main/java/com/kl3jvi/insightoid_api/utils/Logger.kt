package com.kl3jvi.insightoid_api.utils

import android.util.Log


object Logger {
    enum class LogLevel {
        DEBUG, INFO, WARN, ERROR, NONE
    }

    var logLevel: LogLevel = LogLevel.NONE

    fun d(tag: String, message: String) {
        if (logLevel <= LogLevel.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun i(tag: String, message: String) {
        if (logLevel <= LogLevel.INFO) {
            Log.i(tag, message)
        }
    }

    fun w(tag: String, message: String) {
        if (logLevel <= LogLevel.WARN) {
            Log.w(tag, message)
        }
    }

    fun e(tag: String, message: String) {
        if (logLevel <= LogLevel.ERROR) {
            Log.e(tag, message)
        }
    }

    fun e(tag: String, message: String, throwable: Throwable) {
        if (logLevel <= LogLevel.ERROR) {
            Log.e(tag, message, throwable)
        }
    }
}