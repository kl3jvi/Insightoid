package com.kl3jvi.insightoid_api.utils

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission

class NetworkUtils {
    companion object {
        @Volatile
        private var isNetworkConnected = false

        @SuppressLint("MissingPermission")
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkRequest = NetworkRequest.Builder().build()

            connectivityManager.registerNetworkCallback(
                networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        isNetworkConnected = true
                    }

                    override fun onLost(network: Network) {
                        isNetworkConnected = false
                    }
                },
            )

            return isNetworkConnected
        }
    }
}
