package com.heid.games.model.dice.adapter

import com.bumptech.glide.Glide
import com.heid.games.R
import com.heid.games.model.dice.bean.Players
import com.lucas.frame.adapter.BaseQuickAdapter
import com.lucas.frame.adapter.BaseViewHolder
import jp.wasabeef.glide.transformations.CropCircleTransformation

/**
 * @package     com.heid.games.model.dice.adapter
 * @author      lucas
 * @date        2018/9/7
 * @des
 */
class PlayerAdapter : BaseQuickAdapter<Players, BaseViewHolder>(R.layout.item_dice_players) {
    override fun convert(helper: BaseViewHolder, item: Players) {
        if (item.isOnline) {
            if (item.icon.isNotEmpty())
                Glide.with(mContext).load(item.icon).asBitmap().transform(CropCircleTransformation(mContext)).placeholder(R.mipmap.ic_dice_def_icon)
                        .error(R.mipmap.ic_dice_def_icon).into(helper.getView(R.id.v_icon))
            helper.setText(R.id.v_name, item.name)
        } else {
            helper.setText(R.id.v_name, "玩家${item.index}")
        }
//        if (item.icon.isNotEmpty())
        if (helper.adapterPosition == itemCount - 1) {
            if (item.isAddView) helper.setText(R.id.v_name, "")
            Glide.with(mContext).load(R.mipmap.ic_dice_add).asBitmap().transform(CropCircleTransformation(mContext)).placeholder(R.mipmap.ic_dice_def_icon)
                    .error(R.mipmap.ic_dice_def_icon).into(helper.getView(R.id.v_icon))
        }
    }
}