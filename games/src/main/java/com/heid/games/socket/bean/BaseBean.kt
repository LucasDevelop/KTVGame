package com.heid.games.socket.bean

/**
 * @package     com.heid.games.socket.bean
 * @author      lucas
 * @date        2018/9/8
 * @des
 */
open class BaseBean<T:Any>(val action: Int, val msg: String, val status: Int, val data: T? = null) {
}