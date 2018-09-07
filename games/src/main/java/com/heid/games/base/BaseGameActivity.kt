package com.heid.games.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Window
import android.widget.FrameLayout
import android.widget.Toast
import com.blankj.utilcode.util.Utils
import com.heid.games.R
import com.heid.games.utils.FuncUtil
import com.heid.games.utils.ViewUtil
import kotlinx.android.synthetic.main.activity_base.*
import java.util.ArrayList

/**
 * @package     com.heid.games.base
 * @author      lucas
 * @date        2018/8/24
 * @des
 */
abstract class BaseGameActivity : AppCompatActivity(), FuncUtil, ViewUtil {
    val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)// 去除标题
        requestedOrientation = getOrientation()
        setContentView(R.layout.activity_base)
        v_content.addView(LayoutInflater.from(this).inflate(getContentLayoutId(), null))
        v_back.setOnClickListener {
            finish()
        }
        if (getRightView() != 0) {
            v_right_container.addView(LayoutInflater.from(this).inflate(getRightView(), null))
        }
    }

    open fun getOrientation() = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//竖屏


    fun setTitle(msg: String) {
        v_title.text = msg
    }

    fun setBg(@DrawableRes bgRes: Int) {
        v_bg.setImageResource(bgRes)
    }

    fun setBackBg(@DrawableRes id: Int) {
        v_back_bg.setBackgroundResource(id)
    }

    //设置游戏规则
    fun setGameRule() {

    }

    open fun getRightView(): Int = 0

    abstract fun getContentLayoutId(): Int

    val onAcResultList = ArrayList<(requestCode: Int, resultCode: Int, data: Intent?) -> Unit>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onAcResultList.forEach { it(requestCode, resultCode, data) }
    }

    fun String.showToast() {
        Toast.makeText(this@BaseGameActivity, this, Toast.LENGTH_SHORT).show()
    }

}