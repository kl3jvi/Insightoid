package com.kl3jvi.insightoid_api.config

data class InsightoidConfig(
    val apiKey: String,
    val enableCrashReporting: Boolean,
    val enableLogging: Boolean
)