package com.heid.games.pop

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.CalendarContract
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.PopupWindow
import com.heid.games.R

/**
 * @package     com.heid.games.pop
 * @author      lucas
 * @date        2018/9/10
 * @des     游戏规则
 */
class RulePopup(var context: Context) : PopupWindow(context) {
    val root: ViewGroup by lazy { LayoutInflater.from(context).inflate(R.layout.pop_rule, null, false) as ViewGroup }

    init {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.pop_bg)))
        contentView = root
        isFocusable = true
        isOutsideTouchable = true
        val webView = root.findViewById<WebView>(R.id.v_web)
        with(webView.settings) {
            javaScriptEnabled = true
        }
    }

    fun showRule(path: String) {
        root.findViewById<WebView>(R.id.v_web).loadUrl("file:///android_asset/$path")
        showAtLocation((context as Activity).findViewById(android.R.id.content), Gravity.CENTER, 0, 0)
    }

}