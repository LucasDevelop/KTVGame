package com.heid.games.widget

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.view.TextureView
import com.blankj.utilcode.util.SizeUtils
import com.heid.games.R

/**
 * @package     com.heid.games.widget
 * @author      lucas
 * @date        2018/8/27
 * @des  圆周动画
 */
class CircleRunView(context: Context?, attrs: AttributeSet?) : TextureView(context, attrs), TextureView.SurfaceTextureListener, Runnable {
    var mR: Int = 0//半径
    var mCenter = Point()//圆心
    var mThread = Thread(this)
    //绘画频率--值越小速度越快
    val DRAW_RATE = 50
    var isRunning = false
    //设置的padding值，取一个padding值
    private var mPadding: Int = 0
    lateinit var mSpanPaint: Paint

    //盘的背景
    private val mSpanBackground = BitmapFactory.decodeResource(resources, R.mipmap.ic_rotate_path)

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
        mR = measuredWidth / 2 - SizeUtils.dp2px(10f)
        mCenter.x = measuredWidth / 2
        mCenter.y = measuredHeight / 2
        mPadding = paddingLeft
    }

    var startTime = 0L
    var endTime = 0L
    override fun run() {
        while (isRunning) {
            //控制绘画频率
            startTime = SystemClock.currentThreadTimeMillis()
            drawView()
            endTime = SystemClock.currentThreadTimeMillis()
            if (endTime - startTime < DRAW_RATE) {
                //休眠
                SystemClock.sleep(DRAW_RATE - (endTime - startTime))
            }
        }
    }

    lateinit var canvas: Canvas
    private fun drawView() {
        try {
            canvas = lockCanvas()
            //绘画透明背景
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            canvas.drawBitmap(mSpanBackground, null, RectF((mPadding / 2).toFloat(), (mPadding / 2).toFloat(), (measuredWidth - mPadding / 2).toFloat(), (measuredHeight - mPadding / 2).toFloat()), mSpanPaint)
        } catch (e: Exception) {
            //忽略异常
        } finally {
            //释放资源
            unlockCanvasAndPost(canvas)
        }
    }

    fun start() {
        mThread.start()
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
        //初始化绘制Span的画笔
        mSpanPaint = Paint()
        mSpanPaint.setAntiAlias(true)
        mSpanPaint.setDither(true)
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
        isRunning = false
        return false
    }
}