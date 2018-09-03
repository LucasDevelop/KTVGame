package com.heid.game

import android.content.Intent
import android.os.Bundle
import com.heid.games.base.BaseGameActivity
import com.heid.games.model.closeeyes.EyesActivity
import com.heid.games.model.rotate.RotateActivity
import com.heid.games.model.crowd.CrowdNumActivity
import com.heid.games.model.draw.DrawActivity
import com.heid.games.model.spot.SpotNumActivity
import com.heid.games.model.turntable.LuckyTurntableActivity
import com.heid.games.model.undercover.UndercoverActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arrayOf(v_draw, v_lucky, v_spot, v_crowd_num, v_rotate, v_under,v_close_eyes).forEach {
            it.setOnClickListener {
                when (it) {
                    v_draw -> startActivity(Intent(this, DrawActivity::class.java))
                    v_lucky -> startActivity(Intent(this, LuckyTurntableActivity::class.java))
                    v_spot -> startActivity(Intent(this, SpotNumActivity::class.java))
                    v_crowd_num -> startActivity(Intent(this, CrowdNumActivity::class.java))
                    v_rotate -> startActivity(Intent(this, RotateActivity::class.java))
                    v_under -> startActivity(Intent(this, UndercoverActivity::class.java))
                    v_close_eyes -> startActivity(Intent(this, EyesActivity::class.java))
                }
            }
        }
    }
}
