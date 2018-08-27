package com.heid.games.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Window
import android.widget.FrameLayout
import android.widget.Toast
import com.blankj.utilcode.util.Utils
import com.heid.games.R
import kotlinx.android.synthetic.main.activity_base.*

/**
 * @package     com.heid.games.base
 * @author      lucas
 * @date        2018/8/24
 * @des
 */
abstract class BaseGameActivity : AppCompatActivity() {

    //activity生命周期监听
    var onAcDestroy: () -> Unit = {}

    override fun onDestroy() {
        onAcDestroy()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)// 去除标题
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//竖屏
        setContentView(R.layout.activity_base)
        v_content.addView(LayoutInflater.from(this).inflate(getContentLayoutId(), null))
        v_back.setOnClickListener {
            finish()
        }
    }

    fun setTitle(msg: String) {
        v_title.text = msg
    }

    fun setBg(@DrawableRes bgRes:Int){
        v_bg.setBackgroundResource(bgRes)
    }

    abstract fun getContentLayoutId(): Int

    fun String.showToast() {
        Toast.makeText(this@BaseGameActivity, this, Toast.LENGTH_SHORT).show()
    }

}