package com.heid.games.model.spot

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import kotlinx.android.synthetic.main.activity_spot_num.*
import java.util.*

/**
 * @package     com.heid.games.model.spot
 * @author      lucas
 * @date        2018/8/27
 * @version     V1.0
 * @describe    有点你就点
 */

class SpotNumActivity : BaseGameActivity() {
    var currentPosition = 0
    val random = Random()
    var boomPosition = 0
    //数据源
    val resStr = arrayOf("第一最好不相见\n如此便可不相恋", "第二最好不相知\n如此便可不相思", "第三最好不相伴\n如此便可不相欠",
            "第四最好不相惜\n如此便可不相忆", "第五最好不相爱\n如此便可不相弃", "第六最好不相对\n如此便可不相会",
            "第七最好不相误\n如此便可不相负", "第八最好不相许\n如此便可不相续", "第八最好不相许\n如此便可不相续",
            "第十最好不相遇\n如此便可不相聚", "但曾相见便相知\n相见何如不见时", "安得与君相诀绝\n免教生死作相思")

    override fun getContentLayoutId(): Int = R.layout.activity_spot_num


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_spot_bg)
        v_start.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    v_start.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                val intArray = IntArray(2)
                v_start.getLocationInWindow(intArray)
                v_attractView.setAttrPoint(Point(intArray[0], intArray[1]))
            }
        })
        arrayOf(v_start_view, v_num_view, v_show_bt).forEach {
            it.setOnClickListener {
                when (it) {
                    v_start_view -> {
                        //设置中奖位置
                        boomPosition = random.nextInt(resStr.size)
                        showNextNum()
                    }
                    v_num_view -> showNextNum()
                    v_show_bt -> {
                        "show".showToast()
                    }
                }
            }
        }
        v_boom_view.setOnLongClickListener {
            v_start_view.visibility = View.VISIBLE
            v_boom_view.visibility = View.GONE
            v_num_view.visibility = View.GONE
            v_show_bt.visibility = View.GONE
            currentPosition = 0
            return@setOnLongClickListener true
        }
    }

    private fun showNextNum() {
        //判断是否中奖
        if (boomPosition == currentPosition) {
            v_start_view.visibility = View.GONE
            v_boom_view.visibility = View.VISIBLE
            v_num_view.visibility = View.GONE
            v_show_bt.visibility = View.VISIBLE
        } else {
            v_start_view.visibility = View.GONE
            v_boom_view.visibility = View.GONE
            v_show_bt.visibility = View.GONE
            v_num_view.visibility = View.VISIBLE
            v_num.text = (currentPosition + 1).toString()
            v_hint.text = resStr[currentPosition++]
        }
    }
}
