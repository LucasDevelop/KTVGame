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
class EyesKillPersonAdapter : BaseQuickAdapter<EyesIdentity, EyesKillPersonAdapter.UnderHolder>(R.layout.item_eyes_person) {
    var isShowIdentity = false//是否显示身份
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //说话人显示身份
    var speek: String = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    //验人
    var checkIndex = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    //投票
    var vote: Int = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun convert(helper: UnderHolder, item: EyesIdentity) {
        helper.setText(R.id.v_num, item.index.toString())
        helper.getView<View>(R.id.v_num).isEnabled = true
        val imageView = helper.getView<ImageView>(R.id.v_icon)
        imageView.setImageBitmap(item.getIcon())
        helper.setGone(R.id.v_vote_text, isShowIdentity)
        helper.setText(R.id.v_vote_text, item.identity)
        //显示被杀过的人的身份
        if (!item.isAlive) {
            helper.setGone(R.id.v_vote_text, true)
            helper.getView<View>(R.id.v_num).isEnabled = false
            when (item.identity) {
                "警察" -> imageView.setImageResource(R.mipmap.ic_eyes_police_off)
                "杀手" -> imageView.setImageResource(R.mipmap.ic_eyes_killer_off)
                "平民" -> imageView.setImageResource(R.mipmap.ic_eyes_common_off)
            }
        }
        //显示说话人身份
        if (speek == item.identity && item.isAlive) {
            helper.setGone(R.id.v_vote_text, true)
            when (item.identity) {
                "警察" -> imageView.setImageResource(R.mipmap.ic_eyes_police_on)
                "杀手" -> imageView.setImageResource(R.mipmap.ic_eyes_killer_on)
                "平民" -> imageView.setImageResource(R.mipmap.ic_eyes_common_on)
            }
        }
        //显示被验的人身份
        if (checkIndex == item.index && item.isAlive) {
            helper.setGone(R.id.v_vote_text, true)
            when (item.identity) {
                "警察" -> imageView.setImageResource(R.mipmap.ic_eyes_police_on)
                "杀手" -> imageView.setImageResource(R.mipmap.ic_eyes_killer_on)
                "平民" -> imageView.setImageResource(R.mipmap.ic_eyes_common_on)
            }
        }
        //显示投票结果
        if (item.index == vote && item.isAlive) {
            helper.setGone(R.id.v_vote_text, true)
            when (item.identity) {
                "警察" -> imageView.setImageResource(R.mipmap.ic_eyes_police_on)
                "杀手" -> imageView.setImageResource(R.mipmap.ic_eyes_killer_on)
                "平民" -> imageView.setImageResource(R.mipmap.ic_eyes_common_on)
            }
        }
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