package com.heid.game

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * @package     com.heid.game
 * @author      lucas
 * @date        2018/8/27
 * @des
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}