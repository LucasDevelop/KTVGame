package com.heid.games.socket

/**
 * @file       IWrapper.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/5/4 0004
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
open class IWrapper {
    //发送标记
    var action: Int? = null
    var status: Int? = null
    var msg: String? = null

    //参数
    var param:Any? = null

    fun toIWrapper(): IWrapper {
        val iWrapper = IWrapper()
        iWrapper.status = status
        iWrapper.action = action
        iWrapper.msg = msg
        iWrapper.param = param
        return iWrapper
    }
}