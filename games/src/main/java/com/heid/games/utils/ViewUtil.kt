package com.heid.games.utils

import android.app.Activity
import android.media.audiofx.BassBoost
import android.os.Looper
import android.view.View

/**
 * @package     com.heid.games.utils
 * @author      lucas
 * @date        2018/9/3
 * @des
 */
interface ViewUtil {
    fun View.show() {
        if (Thread.currentThread() != Looper.getMainLooper().thread) {
            (context as Activity).runOnUiThread {
                visibility = View.VISIBLE
            }
        } else
            visibility = View.VISIBLE
    }

    fun View.hide(isGone: Boolean = true) {
        if (Thread.currentThread() != Looper.getMainLooper().thread) {
            (context as Activity).runOnUiThread {
                visibility = if (isGone) View.GONE else View.INVISIBLE
            }
        } else
            visibility = if (isGone) View.GONE else View.INVISIBLE
    }

    //切换响应的点击事件
    fun View.setChangeClickListener(click: (View, Boolean) -> Unit) {
        setOnClickListener(object : View.OnClickListener {
            var clickCount = 0
            override fun onClick(p0: View) {
                click(p0, clickCount++ % 2 == 0)
            }
        })
    }
}