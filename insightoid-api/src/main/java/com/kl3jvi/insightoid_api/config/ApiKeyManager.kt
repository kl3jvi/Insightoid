package com.kl3jvi.insightoid_api.config

import android.content.SharedPreferences


class ApiKeyManager(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val API_KEY_PREF = "com.insightoid.sdk.api_key"
    }

    var apiKey: String?
        get() = sharedPreferences.getString(API_KEY_PREF, null)
        set(value) = sharedPreferences.edit().putString(API_KEY_PREF, value).apply()

    fun isApiKeySet(): Boolean {
        return apiKey != null
    }

    fun clearApiKey() {
        sharedPreferences.edit().remove(API_KEY_PREF).apply()
    }
}
