package com.heid.games.model.pointguess

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.model.pointguess.fragment.GameOverFragment
import com.heid.games.model.pointguess.fragment.PlayFragment
import com.heid.games.model.pointguess.fragment.StartFragment
import kotlinx.android.synthetic.main.activity_play_game.*

/**
 * @package     com.heid.games.model.pointguess
 * @author      lucas
 * @date        2018/9/6
 * @version     V1.0
 * @describe    开始游戏
 */
class PlayGameActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_play_game
    val fragments: ArrayList<Fragment> by lazy {
        val list = ArrayList<Fragment>()
        list.add(StartFragment())
        list.add(PlayFragment())
        list.add(GameOverFragment())
        list
    }
    val time: Int by lazy { intent.getIntExtra("time", 0) }

    companion object {
        fun launch(activity: BaseGameActivity, time: Int) {
            val intent = Intent(activity, PlayGameActivity::class.java)
            intent.putExtra("time", time)
            activity.startActivity(intent)
        }
    }

    override fun getOrientation(): Int = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    override fun onCreate(savedInstanceState: Bundle?) {
        //全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_pg_bg)
        setTitle("你画我猜")
        setBackBg(R.drawable.shape_back_y_bg)
        v_pager.setScanScroll(false)
        findViewById<View>(R.id.v_tool).visibility = View.GONE
        v_pager.adapter = GamePagerAdapter()
        v_pager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                if (p0 == 1)
                    (fragments[1] as PlayFragment).startPlay()
                if (p0 == 2) {
                    setBg(R.mipmap.ic_pg_gameover_bg)
                    (fragments[2] as GameOverFragment).refreshView((fragments[1] as PlayFragment).answers)
                }else setBg(R.mipmap.ic_pg_bg)
            }
        })
        //重力传感器
        initAccel()
    }

    var onFront: () -> Unit = {}//前倾
    var onBack: () -> Unit = {}//后倾
    var onVertical: () -> Unit = {}//竖直

    val mListener = object : SensorEventListener {
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

        override fun onSensorChanged(sensorEvent: SensorEvent) {
            if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                var x = 0f
                var z = 0f
                when (display?.rotation) {
                    Surface.ROTATION_0 -> {
                        x = sensorEvent.values[0]
                        z = sensorEvent.values[2]
                    }
                    Surface.ROTATION_90 -> {
                        x = sensorEvent.values[0]
                        z = sensorEvent.values[2]
                    }
                    Surface.ROTATION_180 -> {
                        x = sensorEvent.values[0]
                        z = sensorEvent.values[2]
                    }
                    Surface.ROTATION_270 -> {
                        x = sensorEvent.values[0]
                        z = sensorEvent.values[2]
                    }
                }
                //判断方向
                if (x < 8.5 && z < -6f) {//前倾
                    Log.d("lucas", "x$x,z:$z")
                    Log.d("lucas", "前倾")
                    onFront()
                }
                if (x < 8.5 && z > 6f) {//后倾
                    Log.d("lucas", "x$x,z:$z")
                    Log.d("lucas", "后倾")
                    onBack()
                }
                if (x > 9.5 && z < 1 && z > -1) {//竖直
                    Log.d("lucas", "x$x,z:$z")
                    Log.d("lucas", "竖直")
                    onVertical()
                }
            }
        }
    }
    var display: Display? = null
    var sensorManager: SensorManager? = null

    private fun initAccel() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(mListener, sensor, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(mListener)
    }

    fun nextPage() {
        v_pager.currentItem = v_pager.currentItem + 1
    }

    inner class GamePagerAdapter : FragmentStatePagerAdapter(supportFragmentManager) {
        override fun getItem(p0: Int): Fragment = fragments[p0]
        override fun getCount(): Int = fragments.size
    }
}
