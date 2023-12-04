package com.kl3jvi.insightoid_api.network

import com.kl3jvi.insightoid_api.crashreporting.CrashData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("crash-reports/report")
    suspend fun sendCrashData(@Body crashData: CrashData): Response<Unit>
}
