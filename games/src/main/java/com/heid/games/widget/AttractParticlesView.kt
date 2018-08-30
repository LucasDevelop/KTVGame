package com.heid.games.widget

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import java.util.*

/**
 * @package     com.heid.games.widget
 * @author      lucas
 * @date        2018/8/27
 * @des  粒子背景
 */
class AttractParticlesView(context: Context?, attrs: AttributeSet?) : TextureView(context, attrs), TextureView.SurfaceTextureListener, Runnable {

    //最大粒子数量
    var MAX_PARTICLE = 40
    //粒子运动加速度
    var acceleration = 3
    //绘画频率--值越小速度越快
    val DRAW_RATE = 20
    //粒子半径
    val particleR = SizeUtils.dp2px(5f)
    //粒子汇聚点
    var attractPoint = Point(ScreenUtils.getScreenWidth() / 2, ScreenUtils.getScreenHeight() / 2)
    //当前粒子数量
    var currentPointSize = 0
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

    var attrViewWidth: Int = 0
    var attrViewHeight: Int = 0

    init {
        //常亮
        keepScreenOn = true
        //透明背景
        isOpaque = false
        //监听view创建过程
        surfaceTextureListener = this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = measuredWidth
        viewHeight = measuredHeight
    }

    var index = 0
    override fun run() {
        while (isRunning) {
            //控制绘画频率
            val startTime = SystemClock.currentThreadTimeMillis()
            //判断粒子数量是否达到上限
            if (currentPointSize < MAX_PARTICLE) {
                currentPointSize++
                //开始加入粒子--粒子加入时间随机,位置随机
                val joinTime = (random.nextInt(4) + 1) * 200.toLong()
                var pointX = random.nextInt(viewWidth)
                var pointY = random.nextInt(viewHeight)
                when (index++ % 4) {
                    0 -> pointY = particleR * 2
                    1 -> pointX = viewWidth - particleR * 2
                    2 -> pointY = viewHeight - particleR * 2
                    3 -> pointX = particleR * 2
                }

                handler.postDelayed({
                    if (isRunning)
                        particlePoints.add(APoint(pointX, pointY, attractPoint, handler))
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
            //绘画透明背景
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            //绘制粒子--这里必须要用迭代器
            val iterator = particlePoints.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                canvas.drawCircle(next.x.toFloat(), next.y.toFloat(), particleR.toFloat(), paint)
                next.x += acceleration * (next.y / next.x)
                next.y += acceleration
                //超出控件外或者达到汇聚点的粒子直接移除
                if ((Math.abs(next.x - attractPoint.x) < attrViewWidth / 2 && Math.abs(next.y - attractPoint.y) < attrViewHeight / 2)) {
                    next.isRemove = true
                    iterator.remove()
                    currentPointSize--
                    Log.d("ace", "remove")
                }
            }
        } catch (e: Exception) {
            //忽略异常
        } finally {
            //释放资源
            unlockCanvasAndPost(canvas)
        }
    }

    fun setAttrPoint(p: Point) {
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
        isRunning = false
        return false
    }

    fun Any.log() {
        Log.d("lucas", this.toString())
    }

}