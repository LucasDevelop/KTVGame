package com.heid.games.model.closeeyes

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SizeUtils
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.model.closeeyes.bean.EyesIdentityBean
import kotlinx.android.synthetic.main.activity_eyes_win.*

/**
 * @package     com.heid.games.model.closeeyes
 * @author      lucas
 * @date        2018/9/5
 * @version     V1.0
 * @describe    胜利界面
 */
class EyesWinActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_eyes_win
    val person: ArrayList<EyesIdentityBean> by lazy { intent.getSerializableExtra("person") as ArrayList<EyesIdentityBean> }
    val win: String by lazy { intent.getStringExtra("win") }

    companion object {
        fun launch(activity: BaseGameActivity, person: ArrayList<EyesIdentityBean>, win: String) {
            val intent = Intent(activity, EyesWinActivity::class.java)
            intent.putExtra("person", person)
            intent.putExtra("win", win)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_eyes_play_bg)
        setTitle("天黑请闭眼")
        val dp60 = SizeUtils.dp2px(60f)
        val dp3 = SizeUtils.dp2px(3f)
        if (win == "好人") {
            v_common_win_view.show()
            person.filter { it.identity == "杀手" }.forEach {
                val imageView = ImageView(this)
                val params = LinearLayout.LayoutParams(dp60, dp60)
                params.bottomMargin = dp3
                params.topMargin = dp3
                params.leftMargin = dp3
                params.rightMargin = dp3
                imageView.layoutParams = params
                val bitmap = ImageUtils.scale(BitmapFactory.decodeFile(it.icon), dp60, dp60, true)
                imageView.setImageBitmap(bitmap)
                v_person_contain.addView(imageView)
            }
        } else {
            v_kill_win_view.show()
            person.filter { it.identity == "警察" }.forEach {
                val view = LayoutInflater.from(this).inflate(R.layout.view_vic, v_kill_win_contain, false)
                val imageView = view.findViewById<ImageView>(R.id.v_icon)
                val params = LinearLayout.LayoutParams(dp60, dp60)
                params.leftMargin = dp60 / 6
                params.rightMargin = dp60 / 6
                view.layoutParams = params
                val bitmap = ImageUtils.scale(BitmapFactory.decodeFile(it.icon), dp60, dp60, true)
                imageView.setImageBitmap(bitmap)
                v_kill_win_contain.addView(view)
                v_eyes_win_text.text = if (v_eyes_win_text.text.isBlank()) "${it.index}号" else "和${it.index}号"
            }
            v_eyes_win_text.text = "${v_eyes_win_text.text}是警察"
        }
    }
}
