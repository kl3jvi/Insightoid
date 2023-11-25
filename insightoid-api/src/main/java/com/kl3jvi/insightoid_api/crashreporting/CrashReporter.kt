package com.kl3jvi.insightoid_api.crashreporting

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kl3jvi.insightoid_api.network.NetworkUtils
import com.kl3jvi.insightoid_api.storage.LocalStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CrashReporter(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams), KoinComponent {
    private val localStorage: LocalStorage by inject()

    override suspend fun doWork(): Result {
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
        return true
    }
}
