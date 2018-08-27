package com.heid.games.adapter

import android.widget.ImageButton
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
class CrowdNumAdapter : BaseQuickAdapter<DrawBean, BaseViewHolder>(R.layout.item_crowd_num) {
    var mBoomPosition = 0
    //左右边界
    var leftBoundary = 0
    var rightBoundary = 0
    var isOverGame = false
    override fun convert(helper: BaseViewHolder, item: DrawBean) {
        val position = helper.adapterPosition
        helper.setOnClickListener(R.id.v_bt, {
            if (isOverGame) return@setOnClickListener
            //判断消除范围
            if (position - leftBoundary > rightBoundary - position) {//消除下边
                //重置右边界
                rightBoundary = position - 1
            } else {//消除上边
                //重置左边界
                leftBoundary = position + 1
            }
            //判断是否静音
            if (SystemUtil.isMute(mContext))
            //开启震动
                SystemUtil.startSingleVibrator(mContext, 1000)
            else
                MusicUtil.playMusic(mContext, R.raw.hiscore02)
            //刷新界面
            notifyDataSetChanged()
        })
        helper.setText(R.id.v_num, item.num.toString())
        //已消除的不能再次点击
        helper.getView<ImageButton>(R.id.v_bt).isEnabled = position in leftBoundary..rightBoundary
        helper.setGone(R.id.v_num, position in leftBoundary..rightBoundary)
        //判断炸弹是否被消除
        if (position !in leftBoundary..rightBoundary && position == mBoomPosition) {
            //游戏结束
            helper.setBackgroundRes(R.id.v_bt, R.mipmap.ic_boom_bt)
            onClickBoom(position)
            //判断是否静音
            if (SystemUtil.isMute(mContext))
            //开启震动
                SystemUtil.startVibrator(mContext, 1)
            else
            //播放音效
                MusicUtil.playMusic(mContext, R.raw.failshout)
            //禁止点击
            isOverGame = true
        }
    }

    var onClickBoom: (Int) -> Unit = {}

    override fun setNewData(data: MutableList<DrawBean>?) {
        super.setNewData(data)
        //设置边界
        rightBoundary = data!!.size
    }

    //设置炸弹位置
    fun setBoomPosition(boomPosition: Int) {
        mBoomPosition = boomPosition
    }
}