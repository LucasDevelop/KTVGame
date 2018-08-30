package com.heid.games.model.rotate

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import kotlinx.android.synthetic.main.activity_rotate.*
import java.util.*
import kotlin.math.log
import kotlin.reflect.KProperty

/**
 * @package     com.heid.games.model
 * @author      lucas
 * @date        2018/8/27
 * @version     V1.0
 * @describe    有胆你就转
 */
class RotateActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_rotate
    val random = Random()
    var anim: RotateAnimation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("有胆你就转")
        setBg(R.mipmap.ic_rotate_bg)
        (v_anim.drawable as AnimationDrawable).start()
        v_start.setOnClickListener {
            if (anim == null || (anim != null && anim!!.hasEnded()))
                startAnim()
        }
    }

    var beforDegress = 0f

    fun startAnim() {
        val nextInt = random.nextInt(360).toFloat()
        anim = RotateAnimation(beforDegress, 360f * 9 + nextInt, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f).apply {
            this.interpolator = AccelerateDecelerateInterpolator()
            this.fillAfter = true
            this.duration = 6000
        }
        v_ball.startAnimation(anim)
        beforDegress = nextInt
    }
}

