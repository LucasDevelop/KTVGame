package com.heid.games.utils

import com.heid.games.socket.TCPClient
import com.heid.games.socket.TCPServer
import com.heid.games.socket.bean.BaseBean

/**
 * @package     com.heid.games.utils
 * @author      lucas
 * @date        2018/9/11
 * @des
 */
interface TCPUtil {
    //清楚所有监听
    fun cleatAllTCPListener(){
        TCPClient.cleatAllListener()
        TCPServer.cleatAllListener()
    }

    //发送单条数据
    fun BaseBean<*>.sendData(tag: Any? = null): Boolean {
        //判断端
        if (TCPClient.isOpen) {//客户端
            TCPClient.getClient().sendData(this)
        } else if (TCPServer.isOpen) {//服务端
            TCPServer.connPoll.forEach {
                if (it.tag==tag){
                    it.sendData(this)
                }
            }
        } else {//未连接
            return false
        }
        return true
    }
    //发送数据到所有客户端
    fun BaseBean<*>.sendDataAllToClient(): Boolean {
        if (TCPServer.isOpen){
            TCPServer.connPoll.forEach {
                it.sendData(this)
            }
            return true
        }
        return false
    }
}