package com.kl3jvi.insightoid_api.analytics

data class Event(val name: String, val properties: Map<String, Any> = emptyMap()) {
    companion object {
        fun create(
            name: String,
            properties: Map<String, Any> = emptyMap(),
        ) = Event(name, properties)
    }
}
