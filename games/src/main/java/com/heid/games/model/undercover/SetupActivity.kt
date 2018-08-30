package com.heid.games.model.undercover

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.config.GameConfig
import kotlinx.android.synthetic.main.activity_setup.*
import kotlinx.android.synthetic.main.view_under_setup_right.*

/**
 * @package     com.heid.games.model.undercover
 * @author      lucas
 * @date        2018/8/30
 * @version     V1.0
 * @describe    确认身份
 */
class SetupActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_setup
    override fun getRightView(): Int = R.layout.view_under_setup_right
    var index = 0
    val startTexts = arrayOf(
//            R.mipmap.ic_under_start1,
            R.mipmap.ic_under_start2, R.mipmap.ic_under_start3,
            R.mipmap.ic_under_start4, R.mipmap.ic_under_start5, R.mipmap.ic_under_start6,
            R.mipmap.ic_under_start7, R.mipmap.ic_under_start8, R.mipmap.ic_under_start9,
            R.mipmap.ic_under_start10, R.mipmap.ic_under_start11, R.mipmap.ic_under_start12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_under_setup_bg)
        val personCount = intent.getIntExtra("person_count", 0)
        v_join_count.text = personCount.toString()
        v_under_count.text = GameConfig.personTable[personCount].toString()
        v_start_bt.setOnClickListener {
            if (index / 2 == startTexts.size ) {
                //身份确认完毕
                finish()
                return@setOnClickListener
            }
            v_start_text.setImageResource(if (index++ % 2 == 0) R.mipmap.ic_under_next else startTexts[index / 2-1])
        }
    }
}
