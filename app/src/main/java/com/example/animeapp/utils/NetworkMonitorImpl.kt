package com.example.animeapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkMonitor {


    override val isOnline: Flow<Boolean> = callbackFlow {
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        fun currentStatus(): Boolean {
            val network = connectManager.activeNetwork ?: return false
            val capabilities =
                connectManager.getNetworkCapabilities(network)
            return capabilities?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            ) == true
        }

        // Emit initial state immediately
        trySend(currentStatus())

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        connectManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

}