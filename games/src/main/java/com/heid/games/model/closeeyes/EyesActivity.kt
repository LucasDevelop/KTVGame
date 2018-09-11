package com.heid.games.model.closeeyes

import android.os.Bundle
import android.widget.SeekBar
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.config.GameConfig
import kotlinx.android.synthetic.main.activity_close_eyes.*

/**
 * @package     com.heid.games.model.closeeyes
 * @author      lucas
 * @date        2018/9/1
 * @version     V1.0
 * @describe    天黑请闭眼
 */
class EyesActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_close_eyes
    var personCount = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_eyes_bg)
        setTitle("天黑请闭眼")
        setGameRule("game_rule/tianhei.html")
        v_seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, position: Int, p2: Boolean) {
                personCount = position + 5
                val split = GameConfig.eyesPersonTable[personCount]!!.split("-")
                v_people_num.text = personCount.toString()
                v_kill_num.text = "杀手${split[0]}个"
                v_police_num.text = "警察${split[1]}个"
                v_common_num.text = "平民${split[2]}个"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        v_start.setOnClickListener {
            EyesSetupActivity.launch(this, personCount)
        }
        v_du.setOnClickListener { v_seek.progress = v_seek.progress - 1 }
        v_add.setOnClickListener { v_seek.progress = v_seek.progress + 1 }
    }
}
