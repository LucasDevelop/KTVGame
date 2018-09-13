package com.heid.games.socket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.heid.games.config.GameConfig
import com.heid.games.socket.bean.BaseBean
import com.heid.games.socket.bean.UserInfoBean
import java.io.*
import java.net.*
import java.util.concurrent.Executors
import kotlin.concurrent.thread

/**
 * @package     com.heid.games.socketServer
 * @author      lucas
 * @date        2018/9/7
 * @des   socket服务端
 */
object TCPServer {
    //端口
    var prot = GameConfig.SOCKET_PORT
    private var socketServer: ServerSocket? = null
    //连接池
    val connPoll = ArrayList<ConnTask>()
    var isOpen = false//服务器状态
    private val gson = Gson()
    //线程池
    val threadPoll = Executors.newCachedThreadPool()
    //服务器创建失败
    var onCreateFail: () -> Unit = {}

    //服务器初始化
    fun initServer() {
        try {
            socketServer = ServerSocket()
            socketServer?.reuseAddress = true
            socketServer?.bind(InetSocketAddress(prot))
            isOpen = true
            "初始化服务器".p()
            while (isOpen) {
                "run".p()
                Thread.sleep(20)
                val socket = socketServer?.accept() ?: continue
                val task = ConnTask(socket)
                task.start()
                connPoll.add(task)
                "有新的连接".p()
            }
        } catch (e: BindException) {//端口被占用
            e.printStackTrace()
            onCreateFail()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                socketServer?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //与客户端连接任务
    class ConnTask(val socket: Socket) : Thread() {
        private val reader: InputStream by lazy { socket.getInputStream() }
        private val writer: OutputStream by lazy { socket.getOutputStream() }
        private var threadName: String = ""
        var tag:Any? = null //标记
        var userInfo:UserInfoBean?=null//用户信息

        override fun run() {
            threadName = Thread.currentThread().name
            "run ${Thread.currentThread().name}".p()
            while (isOpen) {
                //判断连接是否断开
                if (socket.isClosed) {//连接断开--用户退出
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
                "$threadName 发送数据:$json".p()
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
        TCPClient.onReceiverSuccess = { action, json, task ->  }
        TCPClient.onReceiverFail = {}
        TCPClient.onUserOutLine = {}
    }

    //关闭服务器
    fun close() {
        isOpen = false
        threadPoll.shutdown()
        socketServer?.close()
    }

    private fun Any.p() {
        Log.d("TCPServer", this.toString())
//        System.out.println("TCPServer:${this}")
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