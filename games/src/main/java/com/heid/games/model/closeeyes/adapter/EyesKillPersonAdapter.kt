package com.heid.games.model.closeeyes.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SizeUtils
import com.heid.games.R
import com.heid.games.model.closeeyes.bean.EyesIdentity
import com.heid.games.model.undercover.bean.PersonInfoBean
import com.lucas.frame.adapter.BaseQuickAdapter
import com.lucas.frame.adapter.BaseViewHolder
import java.io.File

/**
 * @package     com.heid.games.model.undercover.adapter
 * @author      lucas
 * @date        2018/8/31
 * @des
 */
class EyesKillPersonAdapter : BaseQuickAdapter<EyesIdentity, EyesKillPersonAdapter.UnderHolder>(R.layout.item_eyes_person) {
    var isShowIdentity=false//是否显示身份
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var killPersons = ArrayList<EyesIdentity>()//被杀了的人


    override fun convert(helper: UnderHolder, item: EyesIdentity) {
        helper.setText(R.id.v_num, item.index.toString())
        val imageView = helper.getView<ImageView>(R.id.v_icon)
        imageView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                val imgW = imageView.measuredWidth
                val bitmap = ImageUtils.scale(BitmapFactory.decodeFile(item.icon), imgW, imgW, true)
                imageView.setImageBitmap(bitmap)
            }
        })
        helper.setGone(R.id.v_vote_text,isShowIdentity)
        //显示被杀过的人的身份
        if (item in killPersons)
            helper.setGone(R.id.v_vote_text,isShowIdentity)
    }

    fun kill(item: EyesIdentity){
        killPersons.add(item)
        notifyDataSetChanged()
    }


    class UnderHolder(view: View) : BaseViewHolder(view) {

    }
}