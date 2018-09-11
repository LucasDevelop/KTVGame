package com.heid.games.receiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager

/**
 * @package     com.heid.games.receiver
 * @author      lucas
 * @date        2018/9/10
 * @des     监听WiFi连接状态
 */
class WifiStatusReceiver : BroadcastReceiver() {
    companion object {
         var instance: WifiStatusReceiver? = null
        fun registerReceiver(activity: Activity) {
            val filter = IntentFilter()
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)
            filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)
            instance = WifiStatusReceiver()
            activity.registerReceiver(instance, filter)
        }

        fun unregisterReciever(activity: Activity) {
            activity.unregisterReceiver(instance)
            instance = null
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION)
            onWifiStatusChange()
    }

    var onWifiStatusChange: () -> Unit = {}
}