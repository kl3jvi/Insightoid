package com.kl3jvi.insightoid_api.crashreporting


data class CrashData(
    val threadName: String,
    val threadId: Long,
    val exceptionName: String,
    val exceptionMessage: String = "No message provided",
    val stackTrace: String,
)
