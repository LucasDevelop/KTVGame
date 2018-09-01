package com.heid.games.model.undercover.adapter

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
class UnderPersonAdapter : BaseQuickAdapter<PersonInfoBean, UnderPersonAdapter.UnderHolder>(R.layout.item_under_person) {
    //全选
    var isShowHint: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    //单选
    var isShowPosition: Int = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    //投票
    val votes = ArrayList<Int>()

    override fun convert(helper: UnderHolder, item: PersonInfoBean) {
        helper.setText(R.id.v_num, (helper.adapterPosition + 1).toString())
        val imageView = helper.getView<ImageView>(R.id.v_icon)
        imageView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                val imgW = imageView.measuredWidth
                val bitmap = ImageUtils.scale(BitmapFactory.decodeFile(item.iconPath), imgW, imgW, true)
                imageView.setImageBitmap(bitmap)
//                Glide.with(mContext)
//                        .load(File(item.iconPath))
//                        .override(imgW, imgW)
//                        .centerCrop()
//                        .into(helper.getView(R.id.v_icon))
            }
        })
        val el = SizeUtils.dp2px(5f).toFloat()
        helper.getView<CardView>(R.id.v_card).cardElevation = if (isShowHint) el else 0f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            helper.getView<TextView>(R.id.v_num).elevation = if (isShowHint) el else 0f
        }
        if (isShowPosition != -1) {
            helper.getView<CardView>(R.id.v_card).cardElevation = if (isShowPosition == helper.adapterPosition) el else 0f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                helper.getView<TextView>(R.id.v_num).elevation = if (isShowPosition == helper.adapterPosition) el else 0f
            }
        }
        //显示投票
        if (helper.adapterPosition in votes){
        //显示身份
            helper.getView<TextView>(R.id.v_vote_text).let {
                it.visibility = View.VISIBLE
                if (item.isUnder) {
                    it.setBackgroundColor(Color.parseColor("#c0ff0000"))
                    it.text = "卧底"
                } else {
                    it.setBackgroundColor(Color.parseColor("#aa0296d6"))
                    it.text = "平民"
                }
            }
        }
    }


    class UnderHolder(view: View) : BaseViewHolder(view) {

    }
}