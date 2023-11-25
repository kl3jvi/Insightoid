package com.kl3jvi.insightoid_api.crashreporting

import android.content.Context
import com.kl3jvi.insightoid_api.network.ApiClient
import com.kl3jvi.insightoid_api.network.NetworkUtils
import com.kl3jvi.insightoid_api.storage.LocalStorage
import com.kl3jvi.insightoid_api.utils.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.PrintWriter
import java.io.StringWriter

internal class ExceptionHandler(
    private val context: Context,
) : Thread.UncaughtExceptionHandler, KoinComponent {

    private val localStorage: LocalStorage by inject()
    private val apiClient: ApiClient by inject()
    private val scope = CoroutineScope(Dispatchers.IO)
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.e("Error while sending crash data:", "${throwable.message}")
    }

    private val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        try {
            val crashData = collectCrashData(thread, exception)
            localStorage.storeCrashData(crashData)
            if (NetworkUtils.isNetworkAvailable(context)) {
                scope.launch(Dispatchers.IO + errorHandler) {
                    apiClient.sendCrashData(crashData)
                }
            }
        } catch (e: Exception) {
            Logger.e("Error while handling uncaught exception:", "${e.message}")
        } finally {
            defaultExceptionHandler?.uncaughtException(thread, exception)
        }
    }

    private fun collectCrashData(
        thread: Thread,
        exception: Throwable
    ): CrashData {
        val stringWriter = StringWriter()
        exception.printStackTrace(PrintWriter(stringWriter))
        val stackTrace = stringWriter.toString()

        return CrashData(
            threadName = thread.name,
            threadId = thread.id,
            exceptionName = exception.javaClass.name,
            exceptionMessage = exception.message,
            stackTrace = stackTrace
        )
    }
}
