package com.heid.games.model.dice.adapter

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.heid.games.R
import com.heid.games.config.GlideApp
import com.heid.games.model.dice.bean.Players
import com.heid.games.socket.bean.UserInfoBean
import com.heid.games.utils.FuncUtil
import com.lucas.frame.adapter.BaseQuickAdapter
import com.lucas.frame.adapter.BaseViewHolder


/**
 * @package     com.heid.games.model.dice.adapter
 * @author      lucas
 * @date        2018/9/7
 * @des
 */
class PlayerAdapter : BaseQuickAdapter<Players, BaseViewHolder>(R.layout.item_dice_players), FuncUtil {
    override fun convert(helper: BaseViewHolder, item: Players) {
        if (item.isOnline) {
            Log.d("lucas",item.userInfo?.icon)
            val view = helper.getView<ImageView>(R.id.v_icon)
            GlideApp.with(mContext).load(item.userInfo?.icon)
                    .apply(RequestOptions.circleCropTransform().error(R.mipmap.ic_dice_def_icon).placeholder(R.mipmap.ic_dice_def_icon))
                    .into(view)
            helper.setText(R.id.v_name, item.userInfo?.username)
        } else {
            helper.setText(R.id.v_name, "玩家${item.index}")
            helper.setImageResource(R.id.v_icon, R.mipmap.ic_dice_def_icon)
        }
        if (helper.adapterPosition == itemCount - 1) {
            if (item.isAddView) helper.setText(R.id.v_name, "")
            helper.setImageResource(R.id.v_icon, R.mipmap.ic_dice_add)
        }
    }

    //用户上线
    fun addOnlineUser(player: UserInfoBean<Any?>) {
        //判断用户是否已经在席位中
        if (data.find { it.userInfo != null && it.userInfo!!.user_id == player.user_id } != null) {
            "用户已在席位中".l()
            return
        }
        //添加用户到席位中
        val p = data.find { !it.isOnline && !it.isAddView }
        if (p == null) {
            "用户人数已上限".toast(mContext)
            return
        }
        p.userInfo = player
        p.isOnline = true
        notifyDataSetChanged()
    }

    //获取在线用户
    fun getOnlineUsers(): ArrayList<UserInfoBean<Any?>> {
        val list = ArrayList<UserInfoBean<Any?>>()
        data.filter { it.userInfo != null && it.isOnline }.forEach { list.add(it.userInfo!!) }
        return list
    }

    //刷新席位
    fun refreshList(datas: ArrayList<UserInfoBean<Any?>>?) {
        if (datas != null) {
            for (i in 0 until datas.size) {
                data[i].userInfo = datas[i]
                data[i].isOnline = true
            }
        }
        notifyDataSetChanged()
    }
}