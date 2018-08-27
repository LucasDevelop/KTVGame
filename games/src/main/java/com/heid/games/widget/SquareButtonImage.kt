package com.heid.games.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton

/**
 * @package     com.heid.games.widget
 * @author      lucas
 * @date        2018/8/24
 * @des
 */
class SquareButtonImage(context: Context?, attrs: AttributeSet?) : ImageButton(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec))
        val childWidthSize = measuredWidth
        // 高度和宽度一样
        super.onMeasure( MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY),  MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY))

    }
}