package com.heid.games.model.draw

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.heid.games.DrawBean
import com.heid.games.R
import com.heid.games.adapter.DrawAdapter
import com.heid.games.base.BaseGameActivity
import com.heid.games.config.GameConfig
import kotlinx.android.synthetic.main.activity_draw.*
import java.util.*

/**
 * @package     com.heid.games
 * @author      lucas
 * @date        2018/8/24
 * @version     V1.0
 * @describe    扫雷大作战
 */

class DrawActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_draw

    val mAdapter = DrawAdapter()
    //炸弹位置
    var mBoomPosition = Random().nextInt(GameConfig.DRAW_BT_COUNT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("扫雷大作战")
        v_list.layoutManager = GridLayoutManager(this, 4)
        v_list.adapter = mAdapter
        mAdapter.setBoomPosition(mBoomPosition)
        initData()
        initEvent()
    }

    private fun initEvent() {
        mAdapter.onClickBoom = { Toast.makeText(this, "Boom!", Toast.LENGTH_SHORT).show() }
    }

    private fun initData() {
        val list = ArrayList<DrawBean>()
        for (i in 1..GameConfig.DRAW_BT_COUNT) {
            val bean = DrawBean()
            bean.num = i
            list.add(bean)
        }
        mAdapter.setNewData(list)
    }
}
