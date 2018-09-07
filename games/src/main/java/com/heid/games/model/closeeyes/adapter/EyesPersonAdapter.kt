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
import com.heid.games.model.closeeyes.bean.EyesIdentityBean
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
class EyesPersonAdapter : BaseQuickAdapter<EyesIdentity, EyesPersonAdapter.UnderHolder>(R.layout.item_eyes_person) {
    var isShowIdentity=false
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun convert(helper: UnderHolder, item: EyesIdentity) {
        helper.setText(R.id.v_num, item.index.toString())
        val imageView = helper.getView<ImageView>(R.id.v_icon)
        imageView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                val imgW = imageView.measuredWidth
                imageView.setImageBitmap(item.getIcon(imgW,imgW))
            }
        })
        helper.setGone(R.id.v_vote_text,isShowIdentity)
        helper.setText(R.id.v_vote_text,item.identity)
    }

    fun addNewData(d:ArrayList<EyesIdentityBean>){
        val list = ArrayList<EyesIdentity>()
        d.forEach {
            list.add(EyesIdentity(it.index, it.identity, it.icon))
        }
        setNewData(list)
    }

    class UnderHolder(view: View) : BaseViewHolder(view) {

    }
}