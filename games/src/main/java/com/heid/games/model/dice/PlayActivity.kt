package com.heid.games.model.dice

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.google.gson.reflect.TypeToken
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.model.dice.imp.ShakeListener
import com.heid.games.socket.TCPClient
import com.heid.games.socket.TCPServer
import com.heid.games.socket.bean.BaseBean
import com.heid.games.socket.bean.UserInfoBean
import com.heid.games.utils.TCPUtil
import kotlinx.android.synthetic.main.activity_play.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @package     com.heid.games.model.dice
 * @author      lucas
 * @date        2018/9/11
 * @version     V1.0
 * @describe    要骰子界面
 */
class PlayActivity : BaseGameActivity(), TCPUtil {
    override fun getContentLayoutId(): Int = R.layout.activity_play
    val mShakeListener: ShakeListener by lazy { ShakeListener(this) }
    val mAnim: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.anim_glass) }
    var iconImg = arrayListOf(R.mipmap.ic_dice_1, R.mipmap.ic_dice_2, R.mipmap.ic_dice_3, R.mipmap.ic_dice_4, R.mipmap.ic_dice_5, R.mipmap.ic_dice_6)
    val glassView = ArrayList<ImageView>()
    val random = Random()
    var resPoint: String? = null

    companion object {
        fun launch(activity: BaseGameActivity) {
            val intent = Intent(activity, PlayActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_dice_bg)
        setTitle("吹牛")
        setGameRule("game_rule/chuiniu.html")
        glassView.add(v_glass_1)
        glassView.add(v_glass_2)
        glassView.add(v_glass_3)
        glassView.add(v_glass_4)
        glassView.add(v_glass_5)
        closeGlass()
        mShakeListener.setOnShakeListener {
            mShakeListener.stop()
            startAnim()
            val res = StringBuilder()
            glassView.forEach {
                val nextInt = random.nextInt(iconImg.size - 1)
                it.setImageResource(iconImg[nextInt])
                res.append(if (res.isEmpty()) nextInt.toString() else ",$nextInt")
            }
            resPoint = res.toString()
            //如果是客户端，则需要把结果发给服务端
            if (TCPClient.isOpen) {
                val userInfo = TCPClient.getClient().userInfo
                userInfo?.glass_result = resPoint
                BaseBean(0x4, "发送骰子结果", 1, userInfo).sendData()
            } else {//服务器 将结果暂时存起来

            }
            mHandler.postDelayed({ openGlass() }, 700)
        }
        v_open.setOnClickListener {
            if (resPoint == null) {
                "请先摇动骰盅".toast(this)
                return@setOnClickListener
            } else
                OverActivity.launch(this, resPoint, null)
        }
        initServer()
    }

    //初始化服务事件
    private fun initServer() {
        //判断是服务器还是客户端
        if (TCPServer.isOpen) {//服务端
            TCPServer.onReceiverSuccess = { action, json, task ->
                //只有收到所有玩家的骰子结果才能开
                if (action == 0x4) {//收到结果
                    val type = object : TypeToken<BaseBean<UserInfoBean>>() {}.type
                    val bean = mGson.fromJson(json, type) as BaseBean<UserInfoBean>
                    task.userInfo = bean.data
                    //判断所有人是否都摇过骰盅
                    var isAll = true
                    val data = HashMap<Int, Int>()
                    data[1] = 0
                    data[2] = 0
                    data[3] = 0
                    data[4] = 0
                    data[5] = 0
                    data[6] = 0
                    TCPServer.connPoll.forEach {
                        if (it.userInfo == null || it.userInfo?.glass_result == null)
                            isAll = false
                        if (!TextUtils.isEmpty(it.userInfo?.glass_result)) {
                            it.userInfo?.glass_result!!.split(",").forEach {
                                data[it.toInt()] = data[it.toInt()]!! + 1
                            }
                        }
                    }
                    //如果齐了，通知所有人
                    if (isAll) {
                        //加上自己的结果
                        resPoint?.split(",")?.forEach {
                            data[it.toInt()] = data[it.toInt()]!! + 1
                        }
                        BaseBean(0x5, "可以显示结果", 1, mGson.toJson(data)).sendDataAllToClient()
                    }
                }
            }
        } else if (TCPClient.isOpen) {//客户端
            TCPClient.onReceiverSuccess = { action, json, task ->
                if(action == 0x5){//开
                    val type = object : TypeToken<HashMap<Int, Int>>() {}.type
                    val map = mGson.fromJson<HashMap<Int, Int>>(json,type)
                    OverActivity.launch(this,resPoint,map)
                }
            }
        } else {
            //未连接
        }
    }

    //开始动画
    fun startAnim() {
        v_glass_close.startAnimation(mAnim)
    }

    //关上杯子
    private fun closeGlass() {
        mShakeListener.start()
        v_glass_view.show()
        v_hint_text.show()
        v_glass_open.hide()
        v_glass_point.hide()
        v_open.hide()
    }

    //打开杯子
    private fun openGlass() {
        mShakeListener.stop()
        v_glass_view.hide()
        v_hint_text.hide()
        v_glass_open.show()
        v_glass_point.show()
        v_open.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mShakeListener.stop()
    }
}
