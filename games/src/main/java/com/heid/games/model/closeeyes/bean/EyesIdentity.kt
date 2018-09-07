package com.heid.games.model.closeeyes.bean

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SizeUtils
import java.io.Serializable


/**
 * @package     com.heid.games.model.closeeyes.bean
 * @author      lucas
 * @date        2018/9/3
 * @des
 */
class EyesIdentity( index: Int,  identity: String,  icon: String) : EyesIdentityBean( index,  identity,  icon) {
    var bitmap: Bitmap? = null
    fun getIcon(): Bitmap {
        if (bitmap == null) {
            val dp2px = SizeUtils.dp2px(70f)
            bitmap = ImageUtils.scale(BitmapFactory.decodeFile(icon), dp2px, dp2px, true)
        }
        return bitmap!!
    }
    fun getIcon(imgW:Int, imgH:Int): Bitmap {
        if (bitmap == null) {
            bitmap = ImageUtils.scale(BitmapFactory.decodeFile(icon), imgW, imgH, true)
        }
        return bitmap!!
    }
}