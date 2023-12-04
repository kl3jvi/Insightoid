package com.kl3jvi.insightoid_api.crashreporting

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kl3jvi.insightoid_api.network.ApiClient
import com.kl3jvi.insightoid_api.storage.LocalStorage
import com.kl3jvi.insightoid_api.utils.LogTagProvider
import com.kl3jvi.insightoid_api.utils.NetworkUtils
import com.kl3jvi.insightoid_api.utils.info
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CrashReporter(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams), KoinComponent, LogTagProvider {
    private val localStorage: LocalStorage by inject()
    private val apiClient: ApiClient by inject()
    override val TAG: String = "CrashReporter"

    override suspend fun doWork(): Result {
        val crashDataList = localStorage.getCrashData()
        if (NetworkUtils.isNetworkAvailable(applicationContext) && crashDataList.isNotEmpty()) {
            info { "Sending crash data to server" }
            crashDataList.forEach { crashData ->
                val isSentSuccessfully = sendCrashDataToServer(crashData)
                info { "Crash data sent successfully" }
                if (isSentSuccessfully) {
                    localStorage.removeCrashData(crashData.threadId)
                }
            }
        }
        return Result.success()
    }

    private suspend fun sendCrashDataToServer(crashData: CrashData): Boolean {
        val result = apiClient.sendCrashData(crashData)
        return result.isSuccessful
    }

}
