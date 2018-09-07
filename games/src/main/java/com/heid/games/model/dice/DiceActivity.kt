package com.heid.games.model.dice

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.blankj.utilcode.util.NetworkUtils
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.config.GameConfig
import com.heid.games.model.dice.adapter.PlayerAdapter
import com.heid.games.model.dice.bean.Players
import com.heid.games.utils.SystemUtil
import kotlinx.android.synthetic.main.activity_dice.*

/**
 * @package     com.heid.games.model.dice
 * @author      lucas
 * @date        2018/9/7
 * @version     V1.0
 * @describe    吹牛
 */
class DiceActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_dice
    val mAdapter = PlayerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_dice_bg)
        setTitle("吹牛")
        initView()
        initEvent()
    }

    private fun initView() {
        v_list.layoutManager = GridLayoutManager(this, 6)
        v_list.adapter = mAdapter
        //添加自己
        mAdapter.addData(Players(0, "lucas", "https://avatar.csdn.net/B/2/9/1_lucas19911226.jpg?1536301081", true))
        repeat(4, {
            mAdapter.addData(Players(it + 1, "", ""))
        })
        mAdapter.addData(Players(mAdapter.itemCount + 1, "", "", isAddView = true))
        refreshWifiInfo()
    }

    private fun refreshWifiInfo() {
        //判断WiFi是否连接
        if (!NetworkUtils.isWifiConnected()) {
            "WiFi未连接".toast(this)
            v_wifi_name.text = "WiFi未连接"
        } else {
            //获取WiFi名称
            v_wifi_name.text = "${SystemUtil.getWifiName(this)}"
        }
    }

    private fun initEvent() {
        mAdapter.setOnItemClickListener { adapter, view, baseViewHolder, position ->
            if (position == adapter.itemCount - 1) {//添加用户数量
                if (adapter.itemCount - 1 >= GameConfig.MAX_PLAYERS_COUNT) {
                    "玩家人数上限了~~".toast(this)
                    return@setOnItemClickListener
                }
                mAdapter.addData(adapter.itemCount - 1, Players(mAdapter.itemCount + 1, "", ""))
                return@setOnItemClickListener
            }
        }
        arrayOf(v_start, v_create_wifi, v_refresh).forEach {
            it.setOnClickListener {
                when (it) {
                    v_start -> {//开始游戏
                    }
                    v_create_wifi -> {//创建热点
                        val openHotWifi = SystemUtil.openHotWifi(this, true)
                        "打开热点${if (openHotWifi) "成功" else "失败"}".toast(this)
                        refreshWifiInfo()
                    }
                    v_refresh -> {//刷新热点
                        refreshWifiInfo()
                    }
                }
            }
        }
    }
}
