package com.heid.games.widget

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import com.blankj.utilcode.util.SizeUtils
import com.heid.games.R
import java.util.*
import kotlin.math.log

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
    val DRAW_RATE = 3
    var isRunning = false
    //设置的padding值，取一个padding值
    private var mPadding: Int = 0
    lateinit var mSpanPaint: Paint

    //盘的背景
    private val mSpanBackground = BitmapFactory.decodeResource(resources, R.mipmap.ic_rotate_path)
    private val mBall = BitmapFactory.decodeResource(resources, R.mipmap.ic_rotate_ball)
    //小球的速度 0..1
    var speed = 0.001
    var speedMin = 0f
    var speedMax = 0.025
    val acceler = 0.00003//加速度
    var runTime = 0.toDouble()

    lateinit var startPoint: Point
    lateinit var p1: Point
    lateinit var p2: Point
    lateinit var runRange: RectF
    var ballH = 0
    var y_0 = 0
    var x_0 = 0
    var y_2 = 0f
    var x_2 = 0f

    val diffDp = SizeUtils.dp2px(2f)

    //旋转开关
    var isRotate = false
    var isStart = false

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
        val leftPd = SizeUtils.dp2px(9.5f).toFloat()
        val topPd = SizeUtils.dp2px(25f).toFloat()
        val rightPd = SizeUtils.dp2px(9.5f).toFloat()
        val bottomPd = SizeUtils.dp2px(25f).toFloat()
//        val leftPd = 0f
//        val topPd = 0f
//        val rightPd = 0f
//        val bottomPd = 0f
        //x:0 runRange.left
        //y:0 runRange.left
        //x:w runRange.right
        //y:h runRange.bottom
        ballH = mBall.height
        runRange = RectF(leftPd, topPd, measuredWidth - rightPd - ballH, measuredHeight - bottomPd -ballH)
        y_0 = runRange.top.toInt()
        x_0 = runRange.left.toInt()
        y_2 = runRange.bottom / 2 + runRange.top / 2
        x_2 = runRange.right / 2 + runRange.left / 2
        startPoint = Point(x_0, y_2.toInt())
        p1 = Point(x_0 + ballH, y_0 + ballH)
        p2 = Point(x_2.toInt(), y_0)
        mCurrentX = startPoint.x.toDouble()
        mCurrentY = startPoint.y.toDouble()

        Log.d("ace", "w:${runRange.right - runRange.left}  h:${runRange.bottom - runRange.top}")
        Log.d("ace", "x_0:${x_0}  y_0:${y_0}")
        Log.d("ace", "x:${runRange.right}  y:${runRange.bottom}")

    }

    var startTime = 0L
    var endTime = 0L
    lateinit var canvas: Canvas
    override fun run() {
        canvas = lockCanvas()
        //绘画透明背景
        canvas.drawBitmap(mSpanBackground, null, RectF((mPadding / 2).toFloat(), (mPadding / 2).toFloat(), (measuredWidth - mPadding / 2).toFloat(), (measuredHeight - mPadding / 2).toFloat()), mSpanPaint)
        unlockCanvasAndPost(canvas)
        while (isRunning) {
            //控制绘画频率
            startTime = SystemClock.currentThreadTimeMillis()
            drawView()
            endTime = SystemClock.currentThreadTimeMillis()
            if (endTime - startTime < DRAW_RATE) {
                Log.d("sys", "未超载")
                //休眠
                SystemClock.sleep(DRAW_RATE - (endTime - startTime))
            }
        }
    }

    private fun drawView() {
        try {
            canvas = lockCanvas()
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            if (isRotate)
                drawRotateBall()
            else
                canvas.drawBitmap(mBall, mCurrentX.toFloat(), mCurrentY.toFloat(), mSpanPaint)
//            canvas.drawBitmap(mBall, x_0.toFloat(), y_2, mSpanPaint)
//            canvas.drawBitmap(mBall, x_2, y_0.toFloat(), mSpanPaint)
//            canvas.drawBitmap(mBall, runRange.right, y_2, mSpanPaint)
//            canvas.drawBitmap(mBall, x_2, runRange.bottom, mSpanPaint)

//            canvas.drawBitmap(mBall, p1.x.toFloat(), p1.y.toFloat(), mSpanPaint)
        } catch (e: Exception) {
            //忽略异常
        } finally {
            //释放资源
            unlockCanvasAndPost(canvas)
        }
    }

    var mCurrentX = 0.toDouble()
    var mCurrentY = 0.toDouble()
    private fun drawRotateBall() {
        //利用赛贝尔曲线画圆形路径
        //计算坐标
        runTime += speed
        if (runTime > 1) runTime = 0.toDouble()
        mCurrentX = Math.pow(1 - runTime, 2.toDouble()) * startPoint.x + 2 * runTime * (1 - runTime) * p1.x + Math.pow(runTime, 2.toDouble()) * p2.x
        mCurrentY = Math.pow(1 - runTime, 2.toDouble()) * startPoint.y + 2 * runTime * (1 - runTime) * p1.y + Math.pow(runTime, 2.toDouble()) * p2.y
        if (isStart) {
            //加速到匀速
            if (speed < speedMax) {
                //加速
                Log.d("ace", "加速")
                speed += acceler
            } else if (speed >= speedMax) {
                //匀速
                Log.d("ace", "匀速 v=${speed}")
            }
        } else {
            //减速到停止
            Log.d("ace", "减速")
            speed -= acceler
            if (speed <= 0) {
                //停止在随机位置
                isRotate = false
            }
        }
        if (mCurrentX.toInt() == startPoint.x && mCurrentY.toInt() == startPoint.y) {
            Log.d("lucas", "切换节点")
            //mCurrentX:0 runRange.left
            //mCurrentY:0 runRange.left
            //mCurrentX:w runRange.right
            //mCurrentY:h runRange.bottom
            //更换节点
            startPoint.x = p2.x
            startPoint.y = p2.y
            if (p2.x == x_2.toInt() && p2.y == y_0) {
                p1.x = runRange.right.toInt() - ballH+diffDp
                p1.y = y_0 + ballH-diffDp
                p2.x = runRange.right.toInt()
                p2.y = y_2.toInt()
                Log.d("lucas", "1")
            } else if (p2.x == runRange.right.toInt() && p2.y == y_2.toInt()) {
                p1.x = runRange.right.toInt() - ballH+diffDp
                p1.y = runRange.bottom.toInt() - ballH-diffDp
                p2.x = x_2.toInt()
                p2.y = runRange.bottom.toInt()
                Log.d("lucas", "2")
            } else if (p2.x == x_2.toInt() && p2.y == runRange.bottom.toInt()) {
                p1.x = x_0 + ballH-diffDp
                p1.y = runRange.bottom.toInt() - ballH+diffDp
                p2.x = x_0
                p2.y = y_2.toInt()
                Log.d("lucas", "3")
            } else if (p2.x == x_0 && p2.y == y_2.toInt()) {
                p1.x = x_0 + ballH-diffDp
                p1.y = y_0 + ballH+diffDp
                p2.x = x_2.toInt()
                p2.y = y_0
                Log.d("lucas", "4")
            }
            mCurrentX = Math.pow(1 - runTime, 2.toDouble()) * startPoint.x + 2 * runTime * (1 - runTime) * p1.x + Math.pow(runTime, 2.toDouble()) * p2.x
            mCurrentY = Math.pow(1 - runTime, 2.toDouble()) * startPoint.y + 2 * runTime * (1 - runTime) * p1.y + Math.pow(runTime, 2.toDouble()) * p2.y
        }
        canvas.drawBitmap(mBall, mCurrentX.toFloat(), mCurrentY.toFloat(), mSpanPaint)
    }

    fun start() {
        isRotate = true
        //加速旋转
        isStart = true
    }

    fun stop() {
        //减速停止
        isStart = false
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
        //初始化绘制Span的画笔
        mSpanPaint = Paint()
        mSpanPaint.isAntiAlias = true
        mSpanPaint.isDither = true
        isRunning = true
        mThread.start()
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