package com.heid.games.model.dice.bean

import com.heid.games.socket.bean.UserInfoBean

/**
 * @package     com.heid.games.model.dice.bean
 * @author      lucas
 * @date        2018/9/7
 * @des
 */
class Players(var index: Int, var userInfo: UserInfoBean? =null, var isOnline: Boolean = false, var isAddView:Boolean = false) {
}