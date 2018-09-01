package com.heid.games.model.undercover.bean

import android.media.audiofx.BassBoost
import java.io.Serializable

/**
 * @package     com.heid.games.model.undercover.bean
 * @author      lucas
 * @date        2018/8/31
 * @des
 */
class PersonInfoBean (var iconPath:String,var identity:String,var  isUnder:Boolean,var index:Int = 0):Serializable {
    var isLife = true//是否存活
}