package com.heid.games.adapter

import com.heid.games.DrawBean
import com.heid.games.R
import com.heid.games.utils.MusicUtil
import com.heid.games.utils.SystemUtil
import com.lucas.frame.adapter.BaseQuickAdapter
import com.lucas.frame.adapter.BaseViewHolder

/**
 * @package     com.heid.games.adapter
 * @author      lucas
 * @date        2018/8/24
 * @des
 */
class DrawAdapter : BaseQuickAdapter<DrawBean, BaseViewHolder>(R.layout.item_draw) {
    var mBoomPosition = 0
    override fun convert(helper: BaseViewHolder, item: DrawBean) {
        helper.setOnLongClickListener(R.id.v_bt, {
            it.isEnabled = false
            helper.setGone(R.id.v_num, false)
            if (mBoomPosition == helper.adapterPosition) {
                it.setBackgroundResource(R.mipmap.ic_boom_bt)
                onClickBoom(helper.adapterPosition)
                //判断是否静音
                if (SystemUtil.isMute(mContext))
                //开启震动
                    SystemUtil.startVibrator(mContext, 1)
                else
                //播放音效
                    MusicUtil.playMusic(mContext, R.raw.failshout)
            } else {
                //判断是否静音
                if (SystemUtil.isMute(mContext))
                //开启震动
                    SystemUtil.startSingleVibrator(mContext, 1000)
                else
                    MusicUtil.playMusic(mContext, R.raw.hiscore02)
            }
            return@setOnLongClickListener true
        })
        helper.setText(R.id.v_num, item.num.toString())
    }

    var onClickBoom: (Int) -> Unit = {}

    //设置炸弹位置
    fun setBoomPosition(boomPosition: Int) {
        mBoomPosition = boomPosition
    }
}