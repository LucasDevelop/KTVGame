package com.heid.games.widget

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.TextureView

/**
 * @package     com.heid.games.widget
 * @author      lucas
 * @date        2018/8/27
 * @des  圆周动画
 */
class CircleRunView(context: Context?, attrs: AttributeSet?) : TextureView(context, attrs), TextureView.SurfaceTextureListener {


    init {
        //常亮
        keepScreenOn = true
        //透明背景
        isOpaque = false
        //监听view创建过程
        surfaceTextureListener = this
    }

    fun start(){

    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
        return false
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
    }
}