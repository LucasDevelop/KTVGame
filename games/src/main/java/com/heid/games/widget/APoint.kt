package com.heid.games.widget

import android.animation.ValueAnimator
import android.graphics.Point
import android.os.Handler
import android.util.Log
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator

/**
 * @package     com.heid.games.widget
 * @author      lucas
 * @date        2018/8/27
 * @des
 */
class APoint(x: Int, y: Int, var centerPoint: Point, handler: Handler) : Point(x, y) {
    var isRemove =false

    init {
        val startPoint = this
        val endPoint = centerPoint
//        Log.d("APoint","px:${startPoint.x}  py:${startPoint.y}")
//        Log.d("APoint","ex:${endPoint.x}  ey:${endPoint.y}")
        val animator = ValueAnimator.ofObject(PointEvaluator(),startPoint, endPoint)
        animator.addUpdateListener {
            if (isRemove){
                animator.removeAllUpdateListeners()
                return@addUpdateListener
            }
            this.x = (it.animatedValue as Point).x
            this.y = (it.animatedValue as Point).y
//            Log.d("ace","x:${this.x}  y:${this.y}")
        }
        animator.interpolator = AccelerateInterpolator()
        animator.duration = 3000
        handler.post {
            animator.start()
        }
    }

}