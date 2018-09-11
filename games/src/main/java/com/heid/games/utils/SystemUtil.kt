package com.heid.games.utils

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.net.wifi.WifiConfiguration
import android.support.v4.content.ContextCompat.getSystemService
import android.net.wifi.WifiManager
import android.os.Vibrator
import com.blankj.utilcode.util.NetworkUtils


/**
 * @package     com.heid.games.utils
 * @author      lucas
 * @date        2018/8/25
 * @des  系统工具
 */
object SystemUtil {
    //判断是否静音
    fun isMute(context: Context): Boolean {
        return getSystemCurrentVolume(context) == 0
    }

    //获取系统音量
    fun getSystemCurrentVolume(context: Context): Int {
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        max = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)// 1
        return am.getStreamVolume(AudioManager.STREAM_SYSTEM)
    }

    //震动 + 震动次数
    fun startVibrator(context: Context, count: Int) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val patter = longArrayOf(500, 200)
        vibrator.vibrate(patter, count)
    }

    //单次震动
    fun startSingleVibrator(context: Context, time: Int) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(time.toLong())
    }

    //获取WiFi名称
    fun getWifiName(activity: Activity): String? {
        val wifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        return wifiInfo.ssid
    }

    //判断热点是否开启
    fun isOpenApWifi(activity: Activity): Boolean {
        try {
            val wifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val value = wifiManager.javaClass.getDeclaredField("WIFI_AP_STATE_ENABLED").get(wifiManager)
            val isOpen = wifiManager.javaClass.getDeclaredMethod("getWifiApState").invoke(wifiManager)
            return isOpen == value
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //打开/关闭热点
    fun isEnableAPWifi(activity: Activity, isOpen: Boolean, newWifiName: String = "heid", pwd: String = "123456"): Boolean {
        val wifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (isOpen && NetworkUtils.isWifiConnected()) {
            //先关闭wifi
            wifiManager.isWifiEnabled = false
        }
        try {
            //配置热点
            val configuration = WifiConfiguration()
            if (isOpen)
                configuration.apply {
                    //设置热点名称
                    SSID = newWifiName
                    //设置热点密码
                    preSharedKey = pwd
//                hiddenSSID = true
                    allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
                    allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
                    allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
                    allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
                    allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                    allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
                    status = WifiConfiguration.Status.ENABLED
                }
            //反射调用热点
            val method = wifiManager.javaClass.getDeclaredMethod("setWifiApEnabled", WifiConfiguration::class.java, Boolean::class.java)
            val b = method.invoke(wifiManager, configuration, isOpen) as Boolean
            return b
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}