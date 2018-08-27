package com.heid.games.utils

import android.content.Context
import android.media.AudioManager
import android.content.Context.AUDIO_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.content.Context.VIBRATOR_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
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
    fun startSingleVibrator(context: Context,time:Int){
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(time.toLong())
    }
}