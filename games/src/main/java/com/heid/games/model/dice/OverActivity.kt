package com.heid.games.model.dice

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.request.RequestOptions
import com.google.gson.reflect.TypeToken
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.config.GlideApp
import com.heid.games.socket.TCPClient
import com.heid.games.socket.TCPServer
import com.heid.games.socket.bean.BaseBean
import com.heid.games.socket.bean.UserInfoBean
import com.heid.games.utils.TCPUtil
import kotlinx.android.synthetic.main.activity_over.*

/**
 * @package     com.heid.games.model.dice
 * @author      lucas
 * @date        2018/9/8
 * @version     V1.0
 * @describe    游戏结束
 */
class OverActivity : BaseGameActivity(), TCPUtil {
    override fun getContentLayoutId(): Int = R.layout.activity_over
    val resPoint: String by lazy { intent.getStringExtra("resPoint") }
    var iconImg = arrayListOf(R.mipmap.ic_dice_1, R.mipmap.ic_dice_2, R.mipmap.ic_dice_3, R.mipmap.ic_dice_4, R.mipmap.ic_dice_5, R.mipmap.ic_dice_6)
    val mapData: HashMap<Int, Int>? by lazy {
        val extra = intent.getSerializableExtra("map")
        if (extra != null) {
            return@lazy extra as HashMap<Int, Int>
        }
        return@lazy null
    }

    companion object {
        fun launch(activity: BaseGameActivity, resPoint: String?, map: HashMap<Int, Int>?) {
            activity.finish()
            val intent = Intent(activity, OverActivity::class.java)
            intent.putExtra("resPoint", resPoint)
            intent.putExtra("map", map)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_dice_bg)
        setTitle("吹牛")
        setGameRule("game_rule/chuiniu.html")
        initView(mapData)
        initTCPServer()
        v_play_again.setOnClickListener {
            finish()
        }
    }

    private fun initTCPServer() {
        if (TCPServer.isOpen) {
            TCPServer.onReceiverSuccess = { action, json, task ->
                //只有收到所有玩家的骰子结果才能开
                if (action == 0x4) {//收到结果
                    val type = object : TypeToken<BaseBean<UserInfoBean>>() {}.type
                    val bean = mGson.fromJson(json, type) as BaseBean<UserInfoBean>
                    task.userInfo = bean.data
                    //判断所有人是否都摇过骰盅
                    var isAll = true
                    val data = HashMap<Int, Int>()
                    data[0] = 0
                    data[1] = 0
                    data[2] = 0
                    data[3] = 0
                    data[4] = 0
                    data[5] = 0
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
                        resPoint.split(",").forEach {
                            data[it.toInt()] = data[it.toInt()]!! + 1
                        }
                        BaseBean(0x5, "可以显示结果", 1,data).sendDataAllToClient()
                        initView(data)
                    }
                }
            }

        } else if (TCPClient.isOpen) {
            TCPClient.onReceiverSuccess = { action, json, task ->
                if (action == 0x5) {//开
                    val type = object : TypeToken<BaseBean<HashMap<Int, Int>>>() {}.type
                    val map = mGson.fromJson<BaseBean<HashMap<Int, Int>>>(json, type)
                    initView(map.data)
                }
            }
        } else {

        }
    }

    private fun initView(map: HashMap<Int, Int>?) {
        runOnUiThread {
            v_name.text = "lucas"
            GlideApp.with(this).load("https://avatar.csdn.net/B/2/9/3_lucas19911226.jpg").apply(RequestOptions.circleCropTransform()
                    .error(R.mipmap.ic_dice_def_icon).placeholder(R.mipmap.ic_dice_def_icon)).into(v_icon)
            val dp50 = SizeUtils.dp2px(40f)
            v_glass_contain.removeAllViews()
            resPoint.split(",").forEach {
                val imageView = ImageView(this)
                imageView.setImageResource(iconImg[it.toInt()])
                val params = LinearLayout.LayoutParams(dp50, dp50)
                imageView.layoutParams = params
                params.leftMargin = dp50 / 10
                v_glass_contain.addView(imageView)
            }

            if (map != null) {
                v_res_contain.show()
                v_num_top_1.text = (1 + map[0]!!).toString()
                v_num_top_2.text = (1 + map[1]!!).toString()
                v_num_top_3.text = (1 + map[2]!!).toString()
                v_num_top_4.text = (1 + map[3]!!).toString()
                v_num_top_5.text = (1 + map[4]!!).toString()

                v_num_bottom_0.text = map[0].toString()
                v_num_bottom_1.text = map[1].toString()
                v_num_bottom_3.text = map[2].toString()
                v_num_bottom_2.text = map[3].toString()
                v_num_bottom_4.text = map[4].toString()
                v_num_bottom_5.text = map[5].toString()
            }
        }
    }

}
