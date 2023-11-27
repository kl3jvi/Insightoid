package com.kl3jvi.insightoid_api.analytics

import com.kl3jvi.insightoid_api.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class EventTracker(private val apiClient: ApiClient) : CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private val events = mutableListOf<Event>()
    private var batchLimit = 10
    private val flushIntervalMillis = 60_000L // e.g., 60 seconds

    init {
        startPeriodicFlush()
    }

    fun logEvent(event: Event) {
        synchronized(this) {
            events.add(event)
            if (events.size >= batchLimit) {
                flushEvents()
            }
        }
    }

    private fun startPeriodicFlush() {
        launch {
            while (isActive) {
                delay(flushIntervalMillis)
                flushEvents()
            }
        }
    }

    private fun flushEvents() {
        synchronized(this) {
            if (events.isNotEmpty()) {
                try {
                    launch {
                        apiClient.sendEvents(events.toList())
                        events.clear()
                    }
                } catch (e: Exception) {
                    // Handle any errors here (e.g., network issues)
                }
            }
        }
    }

    fun setBatchLimit(limit: Int) {
        batchLimit = limit
    }

    fun stop() {
        job.cancel()
    }
}
