package com.heid.games.utils

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.net.wifi.WifiConfiguration
import android.support.v4.content.ContextCompat.getSystemService
import android.net.wifi.WifiManager
import android.os.Vibrator


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

    //打开热点
    fun openHotWifi(activity: Activity, isOpen: Boolean, newWifiName: String = "heid", pwd: String = "123456"): Boolean {
        val wifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (isOpen) {
            //先关闭wifi
            wifiManager.isWifiEnabled = false
        }
        try {
            //配置热点
            val configuration = WifiConfiguration()
            //设置热点名称
            configuration.SSID = newWifiName
            //设置热点密码
            configuration.preSharedKey = pwd
            //反射调用热点
            val method = wifiManager.javaClass.getDeclaredMethod("setWifiApEnabled", WifiConfiguration::class.java, Boolean::class.java)
            val b = method.invoke(wifiManager, configuration, true) as Boolean
            return b
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}