package com.kl3jvi.insightoid_api.crashreporting

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kl3jvi.insightoid_api.network.NetworkUtils
import com.kl3jvi.insightoid_api.storage.LocalStorage

class CrashReporter(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val localStorage = LocalStorage(applicationContext)
        val crashDataList = localStorage.getCrashData()

        if (NetworkUtils.isNetworkAvailable(applicationContext) && crashDataList.isNotEmpty()) {
            crashDataList.forEach { crashData ->
                // Replace this with your method to send data to the server
                val isSentSuccessfully = sendCrashDataToServer(crashData)

                if (isSentSuccessfully) {
                    localStorage.removeCrashData(crashData.threadId)
                }
            }
        }

        return Result.success()
    }

    private suspend fun sendCrashDataToServer(crashData: CrashData): Boolean {
        // Implement your logic to send crash data to the server
        // Return true if the data is sent successfully, false otherwise
        return true
    }
}