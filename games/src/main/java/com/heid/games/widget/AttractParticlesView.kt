package com.heid.games.widget

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import com.heid.games.base.BaseGameActivity
import java.util.*

/**
 * @package     com.heid.games.widget
 * @author      lucas
 * @date        2018/8/27
 * @des  粒子背景
 */
class AttractParticlesView(context: Context?, attrs: AttributeSet?) : TextureView(context, attrs), TextureView.SurfaceTextureListener, Runnable {

    //最大粒子数量
    var MAX_PARTICLE = 10
    //粒子运动加速度
    var speed = 3
    //绘画频率--值越小速度越快
    val DRAW_RATE = 50
    //粒子半径
    val particleR = 10
    //粒子汇聚点
    var attractPoint = Point(0, 0)
    //当前所有粒子所在的位置
    var particlePoints = ArrayList<APoint>()
    //创建粒子任务
    val task = Thread(this)
    var isRunning = false
    var random = Random()
    var paint = Paint()
    var viewWidth = 0
    var viewHeight = 0
    lateinit var canvas: Canvas
    var save: Int = 0

    init {
        //常亮
        keepScreenOn = true
        //透明背景
        isOpaque = false
        //监听view创建过程
        surfaceTextureListener = this
        //监听页面生命周期
        (context as? BaseGameActivity)?.onAcDestroy = {
            //关闭动画
            isRunning = false
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = measuredWidth
        viewHeight = measuredHeight
    }

    override fun run() {
        while (isRunning) {
            //控制绘画频率
            val startTime = SystemClock.currentThreadTimeMillis()
            //判断粒子数量是否达到上限
            if (particlePoints.size < MAX_PARTICLE) {
                //开始加入粒子--粒子加入时间随机,位置随机
                val joinTime = (random.nextInt(4) + 1) * 200.toLong()
                val pointX = random.nextInt(viewWidth)
                val pointY = random.nextInt(viewHeight)
                handler.postDelayed({
                    particlePoints.add(APoint(pointX, pointY))
                }, joinTime)
            }
            //开始绘画粒子
            drawParticle()
            val endTime = SystemClock.currentThreadTimeMillis()
            if (endTime - startTime < DRAW_RATE) {
                //休眠
                SystemClock.sleep(DRAW_RATE - (endTime - startTime))
            }
        }
    }

    private fun drawParticle() {
        try {
            canvas = lockCanvas()
            save = canvas.save()
            //绘画透明背景
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            //绘制粒子
            particlePoints.forEach {
                canvas.drawCircle(it.x.toFloat(), it.y.toFloat(), particleR.toFloat(), paint)
                it.x += speed*(it.y/it.x)
                it.y += speed
                //超出控件外的粒子直接移除
                if (it.x > viewWidth || it.y > viewHeight) particlePoints.remove(it)
            }
        } catch (e: Exception) {
            //忽略异常
        } finally {
            //释放资源
            canvas.restoreToCount(save)
            unlockCanvasAndPost(canvas)
        }
    }

    fun setAttrPoint(p: Point){
        attractPoint = p
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
        //开始绘画
        //初始化画笔
        with(paint) {
            isAntiAlias = true
            isDither = true
            color = 0xfff6f4ff.toInt()
        }
        //开启任务
        task.start()
        isRunning = true
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
        return false
    }

    fun Any.log(){
        Log.d("lucas",this.toString())
    }

}