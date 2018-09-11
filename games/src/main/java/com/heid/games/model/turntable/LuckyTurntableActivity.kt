package com.heid.games.model.turntable

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.utils.MusicUtil
import com.heid.games.utils.SystemUtil
import kotlinx.android.synthetic.main.activity_lucky_rurntable.*
import java.util.*

/**
 * @package     com.heid.games.model.turntable
 * @author      lucas
 * @date        2018/8/24
 * @version     V1.0
 * @describe    幸运大转盘
 */

class LuckyTurntableActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_lucky_rurntable

    var isRun = false//防止没有点击开始就走回调
    var currentScene = 0//当前场景
    val list = ArrayList<SceneBean>()
    var selectPosition = 0//转盘停止后选中的位置

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_draw_bg)
        setTitle("幸运大转盘")
        //初始化数据
        list.add(SceneBean(arrayOf("上家喝光", "倒满", "喝一半", "喝光", "喝一口", "干杯", "下家喝光", "倒满", "喝一半", "喝光", "剩一口", "跳过"), R.mipmap.ic_jiuchang_strt))
        list.add(SceneBean(arrayOf("讲笑话", "贴纸条", "跳过", "讲故事", "卖萌", "真心话", "摆拍", "讲笑话", "跳过", "做鬼脸", "跳舞", "大冒险"), R.mipmap.ic_lucky_bt_juhui))

        //默认加载第一个
        v_lucky.setOptionTexts(list[currentScene].options)
        iv_start.setImageResource(list[currentScene].startBtRes)

        v_lucky.setOnSpanRollListener {
            if (it == 0.toDouble()) {
                //停下来释放按键
                iv_start.isEnabled = true
                if (isRun)
                    runOnUiThread {
                        list[currentScene].options[selectPosition].showToast()
                        isRun = false
                        //判断是否静音
                        if (SystemUtil.isMute(this))
                        //开启震动
                            SystemUtil.startSingleVibrator(this, 1000)
                    }
            }
        }
        //开始
        iv_start.setOnClickListener {
            //判断是否静音
            if (SystemUtil.isMute(this))
            //开启震动
                SystemUtil.startVibrator(this, 1)
            else
            //开启bgm
                MusicUtil.playMusic(this, R.raw.turntabe)
            isRun = true
            //锁住按键
            iv_start.isEnabled = false
            //传入的参数由后台返回指定中哪个奖项
            val size = list[currentScene].options.size
            selectPosition = Random().nextInt(size)
            selectPosition.toString().showToast()
            v_lucky.luckyStart(selectPosition)
            //模拟请求网络
            Thread(Runnable {
                //网络请求6秒
                SystemClock.sleep(2000)
                //逐渐停止转盘
                v_lucky.luckStop()
            }).start()
        }
        //切换场景
        v_change.setOnClickListener {
            if (isRun) {
                "请先等待转盘停止".showToast()
                return@setOnClickListener
            }
            if (++currentScene == list.size)
                currentScene = 0
            v_lucky.setOptionTexts(list[currentScene].options)
            iv_start.setImageResource(list[currentScene].startBtRes)
        }
    }
}
