package com.heid.games.model.closeeyes.bean

import java.io.Serializable


/**
 * @package     com.heid.games.model.closeeyes.bean
 * @author      lucas
 * @date        2018/9/3
 * @des
 */
open class EyesIdentityBean(val index: Int, val identity: String, val icon: String) : Serializable {
    var killTime = 1L//被杀的时间
    var isAlive = true//是否存活
        set(value) {
            field = value
            if (!field)
                killTime = System.currentTimeMillis()
        }

    override fun toString(): String {
        return "EyesIdentity(index=$index, killTime=$killTime)"
    }
}