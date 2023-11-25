package com.kl3jvi.insightoid_api.storage

import android.content.Context
import com.google.gson.Gson
import com.kl3jvi.insightoid_api.crashreporting.CrashData

class LocalStorage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("CrashData", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun storeCrashData(crashData: CrashData) {
        val crashDataJson = gson.toJson(crashData)
        val editor = sharedPreferences.edit()
        editor.putString(crashData.threadId.toString(), crashDataJson)
        editor.apply()
    }

    fun getCrashData(): List<CrashData> {
        val crashDataList = mutableListOf<CrashData>()
        sharedPreferences.all.forEach { (_, value) ->
            val crashData = gson.fromJson(value as String, CrashData::class.java)
            crashDataList.add(crashData)
        }
        return crashDataList
    }

    fun removeCrashData(threadId: Long) {
        val editor = sharedPreferences.edit()
        editor.remove(threadId.toString())
        editor.apply()
    }
}