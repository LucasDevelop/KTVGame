package com.heid.games.widget

import android.animation.TypeEvaluator
import android.graphics.Point
import android.R.fraction



/**
 * @package     com.heid.games.widget
 * @author      lucas
 * @date        2018/8/29
 * @des
 */
class PointEvaluator: TypeEvaluator<Point>{
    override fun evaluate(fraction: Float, startValue: Point, endValue: Point): Point {
        val x = startValue.x + fraction * (endValue.x - startValue.x)
        val y = startValue.y + fraction * (endValue.y - startValue.y)
        return Point(x.toInt(), y.toInt())
    }
}