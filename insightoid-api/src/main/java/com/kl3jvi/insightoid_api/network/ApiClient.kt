package com.kl3jvi.insightoid_api.network

import com.kl3jvi.insightoid_api.crashreporting.CrashData

class ApiClient(
    private val apiService: ApiService
) {
    suspend fun sendCrashData(crashData: CrashData) = apiService.sendCrashData(crashData)
}