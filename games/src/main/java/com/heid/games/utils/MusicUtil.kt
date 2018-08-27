package com.heid.games.utils

import android.content.Context
import android.media.SoundPool
import android.media.AudioManager
import android.media.AudioAttributes
import android.os.Build



/**
 * @package     com.heid.games.utils
 * @author      lucas
 * @date        2018/8/24
 * @des   音效工具
 */
object  MusicUtil {
    fun playMusic(context:Context,rawId:Int){
        val soundPool: SoundPool
        if (Build.VERSION.SDK_INT >= 21) {
            val builder = SoundPool.Builder()
            //传入音频的数量
            builder.setMaxStreams(1)
            //AudioAttributes是一个封装音频各种属性的类
            val attrBuilder = AudioAttributes.Builder()
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC)
            builder.setAudioAttributes(attrBuilder.build())
            soundPool = builder.build()
        } else {
            //第一个参数是可以支持的声音数量，第二个是声音类型，第三个是声音品质
            soundPool = SoundPool(1, AudioManager.STREAM_SYSTEM, 5)
        }
        //第一个参数Context,第二个参数资源Id，第三个参数优先级
        soundPool.load(context, rawId, 1)
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, status -> soundPool.play(1, 1f, 1f, 0, 0, 1f) }
        //第一个参数id，即传入池中的顺序，第二个和第三个参数为左右声道，第四个参数为优先级，第五个是否循环播放，0不循环，-1循环
        //最后一个参数播放比率，范围0.5到2，通常为1表示正常播放
//        soundPool.play(1, 1, 1, 0, 0, 1);
        //回收Pool中的资源
//        soundPool.release()
    }
}