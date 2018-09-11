package com.heid.games.model.dice

import android.content.Intent
import android.os.Bundle
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
                OverActivity.launch(this,resPoint)
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
                }
            }
        } else if (TCPClient.isOpen) {//客户端
            TCPClient.onReceiverSuccess = { action, json, task ->

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
