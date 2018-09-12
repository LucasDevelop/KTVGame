package com.heid.games.model.dice

import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_over.*

/**
 * @package     com.heid.games.model.dice
 * @author      lucas
 * @date        2018/9/8
 * @version     V1.0
 * @describe    游戏结束
 */
class OverActivity : BaseGameActivity() {
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
    }

    private fun initTCPServer() {
        if (TCPServer.isOpen){
            TCPServer.onReceiverSuccess = {action, json, task ->

            }
        }else if (TCPClient.isOpen){
            TCPClient.onReceiverSuccess = {action, json, task ->
                if(action == 0x5){//开
                    val type = object : TypeToken<HashMap<Int, Int>>() {}.type
                    val map = mGson.fromJson<HashMap<Int, Int>>(json,type)
                    initView(map)
                }
            }
        }else{

        }
    }

    private fun initView(map: HashMap<Int, Int>?) {
        v_name.text = "lucas"
        GlideApp.with(this).load("https://avatar.csdn.net/B/2/9/3_lucas19911226.jpg").apply(RequestOptions.circleCropTransform()
                .error(R.mipmap.ic_dice_def_icon).placeholder(R.mipmap.ic_dice_def_icon)).into(v_icon)
        val dp50 = SizeUtils.dp2px(40f)
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
            v_num_top_1.text = (1 + map[1]!!).toString()
            v_num_top_2.text = (1 + map[2]!!).toString()
            v_num_top_3.text = (1 + map[3]!!).toString()
            v_num_top_4.text = (1 + map[4]!!).toString()
            v_num_top_5.text = (1 + map[5]!!).toString()

            v_num_bottom_0.text = map[0].toString()
            v_num_bottom_1.text = map[1].toString()
            v_num_bottom_2.text = map[2].toString()
            v_num_bottom_3.text = map[3].toString()
            v_num_bottom_4.text = map[4].toString()
            v_num_bottom_5.text = map[5].toString()
        }
    }

}
