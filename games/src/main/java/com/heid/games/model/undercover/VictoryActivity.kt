package com.heid.games.model.undercover

import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SizeUtils
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.model.undercover.bean.PersonInfoBean
import kotlinx.android.synthetic.main.activity_victory.*

/**
 * @package     com.heid.games.model.undercover
 * @author      lucas
 * @date        2018/9/1
 * @version     V1.0
 * @describe    胜利
 */
class VictoryActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_victory
    val victTarget: Int by lazy { intent.getIntExtra("victTarget", 0) }
    val persons: ArrayList<PersonInfoBean> by lazy { intent.getSerializableExtra("persons") as ArrayList<PersonInfoBean> }

    companion object {
        //0卧底胜利 1平民胜利
        fun launch(activity: BaseGameActivity, victTarget: Int, person: ArrayList<PersonInfoBean>) {
            activity.finish()
            val intent = Intent(activity, VictoryActivity::class.java)
            intent.putExtra("victTarget", victTarget)
            intent.putExtra("persons", person)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_under_setup_bg)
        setTitle("谁是卧底")
        if (victTarget == 1) {//平民
            v_vic_icon.setImageResource(R.mipmap.ic_under_common_vct)
            v_under_number.visibility = View.VISIBLE
            v_icon_container.visibility = View.INVISIBLE
            for (i in 0 until persons.size) {
                if (persons[i].isUnder) {
                    val text = v_under_number.text.toString()
                    v_under_number.text = if (text.isEmpty()) persons[i].index.toString() else "${text}、${persons[i].index}"
                }
            }
            v_under_number.text = "${v_under_number.text}号是卧底"
        } else {//卧底
            v_vic_icon.setImageResource(R.mipmap.ic_under_under_vct)
            v_under_number.visibility = View.GONE
            val dp2px = SizeUtils.dp2px(50f)
            persons.filter { it.isUnder }.forEach {
                val view = LayoutInflater.from(this).inflate(R.layout.view_vic, v_icon_container, false) as ViewGroup
                v_icon_container.addView(view)
                val bitmap = ImageUtils.scale(BitmapFactory.decodeFile(it.iconPath), dp2px, dp2px, true)
                view.findViewById<ImageView>(R.id.v_icon).setImageBitmap(bitmap)
                view.findViewById<TextView>(R.id.v_num).text = "${it.index}号"
            }
        }
        for (i in 0 until persons.size) {
            if (persons[i].isUnder) {
                v_under_text.text = "卧底词:${persons[i].identity}"
            } else {
                v_common_text.text = "平民词:${persons[i].identity}"
            }
        }

        v_replay.setOnClickListener {
            finish()
            SetupActivity.launch(this, persons.size)
        }
    }
}
