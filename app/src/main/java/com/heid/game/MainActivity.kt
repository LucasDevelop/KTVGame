package com.heid.game

import android.content.Context
import android.content.Intent
import android.hardware.*
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import com.heid.games.base.BaseGameActivity
import com.heid.games.model.closeeyes.EyesActivity
import com.heid.games.model.closeeyes.EyesPlayActivity
import com.heid.games.model.closeeyes.bean.EyesIdentity
import com.heid.games.model.closeeyes.bean.EyesIdentityBean
import com.heid.games.model.rotate.RotateActivity
import com.heid.games.model.crowd.CrowdNumActivity
import com.heid.games.model.dice.DiceActivity
import com.heid.games.model.draw.DrawActivity
import com.heid.games.model.pointguess.PointGuessActivity
import com.heid.games.model.spot.SpotNumActivity
import com.heid.games.model.turntable.LuckyTurntableActivity
import com.heid.games.model.undercover.UndercoverActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arrayOf(v_draw, v_lucky, v_spot, v_crowd_num, v_rotate, v_under, v_close_eyes, v_point_guess,v_dice).forEach {
            it.setOnClickListener {
                when (it) {
                    v_draw -> startActivity(Intent(this, DrawActivity::class.java))
                    v_lucky -> startActivity(Intent(this, LuckyTurntableActivity::class.java))
                    v_spot -> startActivity(Intent(this, SpotNumActivity::class.java))
                    v_crowd_num -> startActivity(Intent(this, CrowdNumActivity::class.java))
                    v_rotate -> startActivity(Intent(this, RotateActivity::class.java))
                    v_under -> startActivity(Intent(this, UndercoverActivity::class.java))
                    v_close_eyes -> {
//                        EyesPlayActivity.launch(this, arrayListOf(
//                                EyesIdentityBean(1,"杀手","/storage/emulated/0/1536028922019.jpg"),
//                                EyesIdentityBean(2,"警察","/storage/emulated/0/1536028922019.jpg"),
//                                EyesIdentityBean(3,"平民","/storage/emulated/0/1536028922019.jpg"),
//                                EyesIdentityBean(4,"平民","/storage/emulated/0/1536028922019.jpg"),
//                                EyesIdentityBean(5,"平民","/storage/emulated/0/1536028922019.jpg")
//                        ))
                        startActivity(Intent(this, EyesActivity::class.java))
                    }
                    v_point_guess -> startActivity(Intent(this, PointGuessActivity::class.java))
                    v_dice -> startActivity(Intent(this, DiceActivity::class.java))
                }
            }
        }

        //陀螺仪
//        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val display = windowManager.defaultDisplay
//        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//            sensorManager.registerListener(object : SensorEventListener {
//                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//                }
//
//                override fun onSensorChanged(sensorEvent: SensorEvent) {
//                        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
//                            when (display.rotation) {
//                                Surface.ROTATION_0 -> "${sensorEvent.values[0]},${sensorEvent.values[1]},${sensorEvent.values[2]}".p()
//                                Surface.ROTATION_90 -> "${sensorEvent.values[0]},${sensorEvent.values[1]},${sensorEvent.values[2]}".p()
//                                Surface.ROTATION_180 -> "${sensorEvent.values[0]},${sensorEvent.values[1]},${sensorEvent.values[2]}".p()
//                                Surface.ROTATION_270 -> "${sensorEvent.values[0]},${sensorEvent.values[1]},${sensorEvent.values[2]}".p()
//                            }
//                        }
//                    }
//            }, sensor, SensorManager.SENSOR_DELAY_GAME)
    }

    fun Any.p() {
        Log.d("lucas", this.toString())
    }
}
