package com.heid.games.socket

import com.heid.games.config.GameConfig
import java.io.*
import java.net.ServerSocket
import java.net.Socket

/**
 * @package     com.heid.games.socketServer
 * @author      lucas
 * @date        2018/9/7
 * @des   socket服务端
 */
object TCPServer {
    //端口
    val prot = GameConfig.SOCKET_PORT
    var socketServer: ServerSocket? = null
    //连接池
    val connPoll = ArrayList<ConnTask>()
    var isOpen = false//服务器状态

    //服务器初始化
    fun initServer(): Boolean {
        try {
            socketServer = ServerSocket(prot)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //开启服务器
    fun startServer() {
        isOpen = true
        val socket = socketServer?.accept()
        connPoll.add(ConnTask(socket!!))
    }

    //与客户端连接任务
    class ConnTask(val socket: Socket) : Thread() {
        val reader: InputStream by lazy { socket.getInputStream() }
        val writer: OutputStream by lazy { socket.getOutputStream() }

        override fun run() {
            while (isOpen) {
                //判断连接是否断开
                if (socket.isClosed) {//连接断开--用户退出

                } else {
                    //开始读取数据
                    var size = 0//读取的数据大小
                    var content = StringBuilder()
                    while (size != 0) {//如果有数据就一直读
                        size = reader.available()
                        val buffer = ByteArray(size)
                        reader.read(buffer)
                        content.append(String(buffer,0,size))
                    }

                }
            }
        }
    }

    //关闭服务器
    fun close() {
        isOpen = false
    }
}