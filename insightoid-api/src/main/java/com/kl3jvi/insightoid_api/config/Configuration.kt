package com.kl3jvi.insightoid_api.config

/**
 * Configuration options for the Insightoid SDK.
 */
data class Configuration(
    val apiKey: String,
    val enableLogging: Boolean = false,
    val enableCrashReporting: Boolean = false
)