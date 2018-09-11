package com.heid.games.model.dice

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.Gravity
import com.blankj.utilcode.util.NetworkUtils
import com.google.gson.reflect.TypeToken
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.config.GameConfig
import com.heid.games.model.dice.adapter.PlayerAdapter
import com.heid.games.model.dice.bean.Players
import com.heid.games.model.dice.popup.HotWifiPop
import com.heid.games.receiver.WifiStatusReceiver
import com.heid.games.socket.TCPClient
import com.heid.games.socket.TCPServer
import com.heid.games.socket.bean.BaseBean
import com.heid.games.socket.bean.UserInfoBean
import com.heid.games.utils.SystemUtil
import com.heid.games.utils.TCPUtil
import kotlinx.android.synthetic.main.activity_dice.*

/**
 * @package     com.heid.games.model.dice
 * @author      lucas
 * @date        2018/9/7
 * @version     V1.0
 * @describe    吹牛
 */
class DiceActivity : BaseGameActivity() ,TCPUtil{
    override fun getContentLayoutId(): Int = R.layout.activity_dice
    val mAdapter = PlayerAdapter()
    val mPopup: HotWifiPop by lazy { HotWifiPop(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_dice_bg)
        setTitle("吹牛")
        setGameRule("game_rule/chuiniu.html")
        initView()
        initEvent()

        //判断是否开启热点
        if (SystemUtil.isOpenApWifi(this))
            createServer()
        else if (NetworkUtils.isWifiConnected())//如果以连接WiFi，尝试去连接服务器
            joinRoom()
    }

    //尝试通过当前wifi加入房间
    private fun joinRoom() {
        //判断WiFi是否连接
        if (!NetworkUtils.isWifiConnected()) return
        //解析WiFi名称获取端口号
        val wifiName = SystemUtil.getWifiName(this) ?: return
        if (!wifiName.contains("_")) return
        val split = wifiName.split("_")
        //重置端口
        if (TextUtils.isDigitsOnly(split[split.size - 1])) TCPClient.prot = split[split.size - 1].toInt()
        TCPClient.threadPoll.execute({
            val isConn = TCPClient.connServer("192.168.43.1")
            if (isConn){
                val bean = BaseBean(0x01, "加入房间", 1, UserInfoBean(10, "ace", "https://avatar.csdn.net/B/2/9/3_lucas19911226.jpg"))
                TCPClient.getClient().sendData(bean)
                TCPClient.getClient().userInfo = bean.data
            }
        })
        //收到消息
        TCPClient.onReceiverSuccess = { action, json, task ->
            runOnUiThread {
                when (action) {
                    0x2 -> {//刷新席位列表
                        val data = mGson.fromJson(json, object : TypeToken<BaseBean<ArrayList<UserInfoBean>>>() {}.type) as BaseBean<ArrayList<UserInfoBean>>
                        mAdapter.refreshList(data.data)
                    }
                    0x3 -> {//开始游戏
                        TCPClient.cleatAllListener()
                        PlayActivity.launch(this)
                    }
                }
            }
        }
    }

    private fun initView() {
        v_list.layoutManager = GridLayoutManager(this, 6)
        v_list.adapter = mAdapter
        //添加自己
        mAdapter.addData(Players(0, UserInfoBean(1, "lucas", "https://avatar.csdn.net/B/2/9/3_lucas19911226.jpg"), true))
        repeat(4, {
            mAdapter.addData(Players(it + 1, null))
        })
        mAdapter.addData(Players(mAdapter.itemCount + 1, null, isAddView = true))
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
        //监听网络状态
        WifiStatusReceiver.registerReceiver(this)
        WifiStatusReceiver.instance?.onWifiStatusChange = {
            refreshWifiInfo()
            "change".l()
            if (NetworkUtils.isWifiConnected())//如果以连接WiFi，尝试去连接服务器
                joinRoom()
        }
        mPopup.onCreateHotWifi = { name, pwd ->
            val openHotWifi = SystemUtil.isEnableAPWifi(this, true, "${name}_${TCPServer.prot}", pwd)
            "打开热点${if (openHotWifi) "成功" else "失败"}".toast(this)
            createServer()
        }
        mAdapter.setOnItemClickListener { adapter, view, baseViewHolder, position ->
            if (position == adapter.itemCount - 1) {//添加用户数量
                if (adapter.itemCount - 1 >= GameConfig.MAX_PLAYERS_COUNT) {
                    "玩家人数上限了~~".toast(this)
                    return@setOnItemClickListener
                }
                mAdapter.addData(adapter.itemCount - 1, Players(mAdapter.itemCount + 1, null))
                return@setOnItemClickListener
            }
        }
        arrayOf(v_start, v_create_wifi, v_refresh).forEach {
            it.setOnClickListener {
                when (it) {
                    v_start -> {//开始游戏
                        //链接上游戏后开始游戏
                        if (TCPServer.isOpen || TCPClient.isOpen){
                            cleatAllTCPListener()
                            //通知其他人开始游戏
                            val bean = BaseBean(0x3, "开始游戏", 1, null)
                            if (TCPServer.isOpen) bean.sendDataAllToClient() else bean.sendData()
                            PlayActivity.launch(this)
                        }
                        else
                            "请先链接热点或者创建热点".toast(this)
                    }
                    v_create_wifi -> {//创建热点
                        mPopup.showAtLocation(it, Gravity.CENTER, 0, 0)
                    }
                    v_refresh -> {//刷新热点
                        refreshWifiInfo()
                    }
                }
            }
        }
    }

    private fun createServer() {
        TCPServer.onCreateFail = {
            //断开热点重新创建
            SystemUtil.isEnableAPWifi(this, false)
        }
        //创建服务器
        TCPServer.threadPoll.execute {
            TCPServer.initServer()
        }
        refreshWifiInfo()
        mPopup.dismiss()
        //收到消息
        TCPServer.onReceiverSuccess = { action, json, task ->
            runOnUiThread {
                when (action) {
                    0x1 -> {//加入房间
                        val bean = joinRoom(json)
                        task.tag = bean.data?.user_id
                        task.userInfo = bean.data
                        //返回席位列表
                        task.sendData(BaseBean(0x2, "连接成功", 1, mAdapter.getOnlineUsers()))
                    }
                    0x3 -> {//开始游戏
                        cleatAllTCPListener()
                        //通知其他人开始游戏
                        BaseBean(0x3, "开始游戏", 1, null).sendDataAllToClient()
                        PlayActivity.launch(this)
                    }
                }
            }
        }
    }

    private fun joinRoom(json: String): BaseBean<UserInfoBean> {
        val bean = mGson.fromJson(json, object : TypeToken<BaseBean<UserInfoBean>>() {}.type) as BaseBean<UserInfoBean>
        mAdapter.addOnlineUser(bean.data!!)
        return bean
    }

    override fun onDestroy() {
        super.onDestroy()
        TCPServer.close()
        WifiStatusReceiver.unregisterReciever(this)
    }
}
