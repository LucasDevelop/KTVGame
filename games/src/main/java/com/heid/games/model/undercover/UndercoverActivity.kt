package com.heid.games.model.undercover

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.config.GameConfig
import kotlinx.android.synthetic.main.activity_undercover.*

/**
 * @package     com.heid.games.model.undercover
 * @author      lucas
 * @date        2018/8/29
 * @version     V1.0
 * @describe    谁是卧底
 */
class UndercoverActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_undercover

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_under_bg)
        setTitle("谁是卧底")
        arrayOf(v_plus, v_reduce, v_double_play, v_single_play, v_start).forEach {
            it.setOnClickListener {
                when (it) {
                    v_plus -> v_brightness.progress += 1
                    v_reduce -> v_brightness.progress -= 1
                    v_double_play -> {
                        val intent = Intent(this, SetupActivity::class.java)
                        intent.putExtra("person_count", 8)
                        startActivity(intent)
                    }
                    v_single_play -> {
                        val intent = Intent(this, SetupActivity::class.java)
                        intent.putExtra("person_count", 4)
                        startActivity(intent)
                    }
                    v_start -> {
                        val intent = Intent(this, SetupActivity::class.java)
                        intent.putExtra("person_count", v_brightness.progress + 4)
                        startActivity(intent)
                    }
                }
            }
        }
        v_brightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val progress = p1 + 4
                v_join_person.text = "参与人数$progress"
                v_under_person.text = "卧底人数${GameConfig.underPersonTable.getValue(progress)}"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }
}
