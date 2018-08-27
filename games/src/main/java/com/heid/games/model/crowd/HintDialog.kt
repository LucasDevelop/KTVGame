package com.heid.games.model.crowd

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.blankj.utilcode.util.ScreenUtils
import com.heid.games.R

/**
 * @package     com.heid.games.model.crowd
 * @author      lucas
 * @date        2018/8/27
 * @des
 */
class HintDialog(var context: Context) : PopupWindow(context) {
    init {
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_crowd_num, null, false)
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    fun showDialog() {
        val view = (context as Activity).findViewById<View>(android.R.id.content)
        showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, -ScreenUtils.getScreenHeight() / 4)
    }
}