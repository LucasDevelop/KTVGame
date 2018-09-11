package com.heid.games.model.crowd

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.heid.games.DrawBean
import com.heid.games.R
import com.heid.games.adapter.CrowdNumAdapter
import com.heid.games.adapter.DrawAdapter
import com.heid.games.base.BaseGameActivity
import com.heid.games.config.GameConfig
import kotlinx.android.synthetic.main.activity_corwd_num.*
import java.util.*

/**
 * @package     com.heid.games.model.crowd
 * @author      lucas
 * @date        2018/8/27
 * @version     V1.0
 * @describe    疯狂挤数字
 */

class CrowdNumActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_corwd_num
    //炸弹位置
    var mBoomPosition = Random().nextInt(GameConfig.CROWD_NUM_COUNT)
    val mAdapter = CrowdNumAdapter()
    val mHintDialog :HintDialog by lazy { HintDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_draw_bg)
        setTitle("疯狂挤数字")
        v_list.layoutManager = GridLayoutManager(this, 8)
        v_list.adapter = mAdapter
        mAdapter.setBoomPosition(mBoomPosition)
        initData()
        initEvent()
    }

    private fun initEvent() {
        mAdapter.onClickBoom = { mHintDialog.showDialog() }
    }

    private fun initData() {
        val list = ArrayList<DrawBean>()
        for (i in 1..GameConfig.CROWD_NUM_COUNT) {
            val bean = DrawBean()
            bean.num = i
            list.add(bean)
        }
        mAdapter.setNewData(list)
    }
}
