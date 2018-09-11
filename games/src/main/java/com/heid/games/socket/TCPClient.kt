package com.heid.games.socket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.heid.games.config.GameConfig
import com.heid.games.socket.bean.BaseBean
import com.heid.games.socket.bean.UserInfoBean
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.util.concurrent.Executors
import kotlin.concurrent.thread

/**
 * @package     com.heid.games.socket
 * @author      lucas
 * @date        2018/9/8
 * @des     socket 客户端
 */
object TCPClient {
    private val gson = Gson()
    private var client: ConnTask? = null
    var isOpen = false
    //线程池
    val threadPoll = Executors.newCachedThreadPool()
    var prot = GameConfig.SOCKET_PORT

    //连接服务器
    fun connServer(ip: String): Boolean {
        try {
            val socket = Socket(ip, prot)
            isOpen = true
            client = ConnTask(socket)
            client?.start()
            return true
            "链接服务器成功 ip:${ip},prot:${prot}".p()
        } catch (e: Exception) {
            e.printStackTrace()
            "链接服务器失败".p()
            return false
        }
    }

    //获取客户端
    fun getClient(): ConnTask {
        return client!!
    }

    //与服务端连接任务
    class ConnTask(val socket: Socket) : Thread() {
        private val reader: InputStream by lazy { socket.getInputStream() }
        private val writer: OutputStream by lazy { socket.getOutputStream() }
        private var threadName: String = ""
        var userInfo: UserInfoBean?=null//用户信息

        override fun run() {
            threadName = Thread.currentThread().name
            while (TCPClient.isOpen) {
                //判断连接是否断开
                if (socket.isClosed) {//连接断开--用户掉线
                    onUserOutLine()
                } else {
                    //开始读取数据
                    var size = reader.available()//读取的数据大小
                    var content = StringBuilder()
                    while (size != 0) {//如果有数据就一直读
                        size = reader.available()
                        val buffer = ByteArray(size)
                        reader.read(buffer)
                        content.append(String(buffer, 0, size))
                    }
                    if (content.isEmpty()) continue
                    "${threadName}接收到数据：$content".p()
                    if (content.isNotEmpty() && isJson(content.toString())) {
                        //解析数据
                        val baseBean = gson.fromJson(content.toString(), BaseBean::class.java)
                        if (baseBean.status == 1) {
                            onReceiverSuccess(baseBean.action, content.toString(), this)
                        } else {
                            onReceiverFail(baseBean)
                        }
                    }
                }
                //降低调用频率
                Thread.sleep(10)
            }
        }

        //发送数据
        fun sendData(bean: BaseBean<*>) {
            threadPoll.execute {
                val json = gson.toJson(bean)
                writer.write(json.toByteArray())
                writer.flush()
                "${threadName}发送数据:$json".p()
            }
        }

    }

    //接收数据
    var onReceiverSuccess: (action: Int, json: String, task: ConnTask) -> Unit = { action, json, task -> }
    var onReceiverFail: (BaseBean<*>) -> Unit = {}
    //用户掉线
    var onUserOutLine: () -> Unit = {}

    //取消所有监听
    fun cleatAllListener(){
        onReceiverSuccess = {action, json, task ->  }
        onReceiverFail = {}
        onUserOutLine = {}
    }

    //停止连接
    fun stop() {
        isOpen = false
        client?.socket?.close()
    }

    fun Any.p() {
        Log.d("TCPClient", this.toString())
//        System.out.println("TCPClient:${this}")
    }

    /**
     * 判断字符串是否为json格式
     */
    private fun isJson(json: String): Boolean {
        if (json.isEmpty()) return false
        return try {
            JsonParser().parse(json)
            true
        } catch (e: Exception) {
            false
        }
    }
}