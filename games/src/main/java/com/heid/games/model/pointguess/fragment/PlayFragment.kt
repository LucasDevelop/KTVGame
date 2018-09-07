package com.heid.games.model.pointguess.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.view.menu.MenuAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.heid.games.R
import com.heid.games.model.pointguess.PlayGameActivity
import com.heid.games.model.pointguess.bean.AnswerBean
import com.heid.games.utils.ViewUtil
import com.heid.games.widget.CustomSeekBar
import java.util.*
import kotlin.collections.ArrayList

/**
 * @package     com.heid.games.model.pointguess.fragment_pg_gameover
 * @author      lucas
 * @date        2018/9/6
 * @des  正在游戏
 */
class PlayFragment : Fragment(), ViewUtil {
    val mHander = Handler()
    var delayTime = 0L
    val task = DownTimeTask()
    val time: Int by lazy { (activity as PlayGameActivity).time }
    var mSeekBar: CustomSeekBar? = null
    var mText: TextView? = null
    var mTimeView: TextView? = null
    var mCloseView: View? = null
    var mIconView: ImageView? = null
    var mBgColorView: View? = null
    val keyWorldArr: Array<String> by lazy { resources.getStringArray(R.array.poingguessdata) }
    var keyWorld: String = ""
    val mActivity: PlayGameActivity by lazy { activity as PlayGameActivity }
    val random = Random()
    //答过的题
    val answers = ArrayList<AnswerBean>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = LayoutInflater.from(context).inflate(R.layout.fragment_pg_play, null, false)!!
        initView(inflate)
        return inflate
    }

    private fun initView(inflate: View) {
        mSeekBar = inflate.findViewById(R.id.v_progress)
        mText = inflate.findViewById(R.id.v_text)
        mTimeView = inflate.findViewById(R.id.v_time)
        mCloseView = inflate.findViewById(R.id.v_close)
        mIconView = inflate.findViewById(R.id.v_icon)
        mBgColorView = inflate.findViewById(R.id.v_bg_color)
        mCloseView?.setOnClickListener { activity?.finish() }
        mSeekBar?.max = time
        mSeekBar?.setScanScroll(false)
        //获取关键字
        keyWorld = keyWorldArr[random.nextInt(keyWorldArr.size - 1)]
    }

    //设置锁，防止回调重复调用
    var isLock = true

    fun startPlay() {
        delayTime += 2000
        mHander.postDelayed({
            mText?.text = 3.toString()
        }, delayTime)
        delayTime += 1000
        mHander.postDelayed({
            mText?.text = 2.toString()
        }, delayTime)
        delayTime += 1000
        mHander.postDelayed({
            mText?.text = 1.toString()
        }, delayTime)
        delayTime += 1000
        mHander.postDelayed({
            mCloseView?.hide()
            mText?.text = keyWorld
            task.start()
            initCallback()
        }, delayTime)
    }

    private fun initCallback() {
        //竖直
        mActivity.onVertical = {
            if (isLock)
                isLock = false
            mText?.text = keyWorld
            mText?.show()
            mIconView?.hide()
            mBgColorView?.setBackgroundColor(resources.getColor(R.color.pg_bg_normal))
        }
        //前倾--正确
        mActivity.onFront = {
            if (!isLock) {
                answers.add(AnswerBean(keyWorld, true))
                //重新获取关键字
                keyWorld = keyWorldArr[random.nextInt(keyWorldArr.size - 1)]
                mIconView?.setImageResource(R.mipmap.ic_answer_success)
                mIconView?.show()
                mText?.hide()
                mTimeView?.hide()
                mBgColorView?.setBackgroundColor(resources.getColor(R.color.pg_bg_right))
                isLock = true
            }
        }
        //后倾--跳过
        mActivity.onBack = {
            if (!isLock) {
                answers.add(AnswerBean(keyWorld, false))
                //重新获取关键字
                keyWorld = keyWorldArr[random.nextInt(keyWorldArr.size - 1)]
                mIconView?.setImageResource(R.mipmap.ic_answer_jump)
                mIconView?.show()
                mText?.hide()
                mTimeView?.hide()
                mBgColorView?.setBackgroundColor(resources.getColor(R.color.pg_bg_skip))
                isLock = true
            }
        }
    }

    inner class DownTimeTask : Runnable {
        var t: Int = 0
        var isRun = false
        override fun run() {
            if (!isRun) return
            mTimeView?.show()
            mTimeView?.text = formatTime(t)
            mSeekBar?.progress = t
            t--
            mTimeView?.setTextColor(Color.parseColor(if (t < 10) "#ff6c47" else "#ffdc47"))
            if (t < 0) {
                mIconView?.setImageResource(R.mipmap.ic_answer_close)
                mIconView?.show()
                mText?.hide()
                mTimeView?.hide()
                mBgColorView?.setBackgroundColor(resources.getColor(R.color.pg_bg_over))
                stop()
                mHander.postDelayed({mActivity.nextPage()},1000)
                return
            } else {
                mHander.postDelayed(this, 1000)
            }
        }

        fun start() {
            t = time
            isRun = true
            mHander.post(this)
        }

        fun stop() {
            isRun = false
            mHander.removeCallbacks(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        task.stop()
    }

    fun formatTime(time: Int): String {
        val m = time / 60
        val s = time % 60
        val builder = StringBuilder()
        if (m < 10)
            builder.append("0$m:")
        else
            builder.append("$m:")
        if (s < 10)
            builder.append("0$s")
        else
            builder.append("$s")
        return builder.toString()
    }
}