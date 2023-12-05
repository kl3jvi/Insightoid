package com.kl3jvi.insightoid_api.crashreporting

import android.content.Context
import com.kl3jvi.insightoid_api.config.InsightoidConfig
import com.kl3jvi.insightoid_api.network.ApiClient
import com.kl3jvi.insightoid_api.storage.LocalStorage
import com.kl3jvi.insightoid_api.utils.LogTagProvider
import com.kl3jvi.insightoid_api.utils.NetworkUtils
import com.kl3jvi.insightoid_api.utils.debug
import com.kl3jvi.insightoid_api.utils.error
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.PrintWriter
import java.io.StringWriter

class ExceptionHandler(
    private val context: Context,
) : Thread.UncaughtExceptionHandler, KoinComponent, LogTagProvider {
    override val TAG: String = "ExceptionHandler"

    private val localStorage: LocalStorage by inject()
    private val apiClient: ApiClient by inject()
    private val config: InsightoidConfig by inject()

    private val scope = CoroutineScope(Dispatchers.IO)
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        error(throwable) { "Error while sending crash data to server" }
    }

    private val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(
        thread: Thread,
        exception: Throwable,
    ) {
        try {
            val crashData = collectCrashData(thread, exception)
            localStorage.storeCrashData(crashData)
            if (NetworkUtils.isNetworkAvailable(context) && config.enableCrashReporting) {
                scope.launch(Dispatchers.IO + errorHandler) {
                    debug { "Sending crash data to server" }
                    val result = apiClient.sendCrashData(crashData)
                    if (result.isSuccessful) {
                        debug { "Crash data sent successfully" }
                        localStorage.removeCrashData(crashData.threadId)
                    } else {
                        error { "Error while sending crash data to server" }
                    }
                }
            }
        } catch (e: Exception) {
            error(e) { "Error while handling uncaught exception" }
        } finally {
            defaultExceptionHandler?.uncaughtException(thread, exception)
        }
    }

    private fun collectCrashData(
        thread: Thread,
        exception: Throwable,
    ): CrashData {
        val stringWriter = StringWriter()
        exception.printStackTrace(PrintWriter(stringWriter))
        val stackTrace = stringWriter.toString()

        return CrashData(
            threadName = thread.name,
            threadId = thread.id,
            exceptionName = exception.javaClass.name,
            exceptionMessage = exception.message.orEmpty(),
            stackTrace = stackTrace,
            uniqueIdentifier = localStorage.getUserId()
        )
    }

}
