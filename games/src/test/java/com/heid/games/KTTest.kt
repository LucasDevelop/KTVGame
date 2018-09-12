package com.heid.games

import android.os.SystemClock
import com.google.gson.Gson
import com.heid.games.socket.TCPClient
import com.heid.games.socket.TCPServer
import com.heid.games.socket.bean.BaseBean
import org.junit.Test
import java.net.InetAddress
import kotlin.concurrent.thread

/**
 * @package     com.heid.games
 * @author      lucas
 * @date        2018/8/29
 * @des
 */
class KTTest {
    @Test
    fun test() {
//        InetAddress.getLocalHost().hostAddress.p()
//        Thread({
//                Thread.sleep(500)
//            TCPClient.connServer("169.254.209.140")
//            TCPClient.getClient().sendData(BaseBean(0x01, "send msg", 1))
//        }).start()
//        //开启服务器
//        TCPServer.initServer()
        val map = Gson().fromJson("{\"1\":2,\"2\":0,\"3\":3}", HashMap::class.java)
        map.forEach { key, value ->
            "k:$key,v:$value".p()
        }
    }

    fun Any.p() {
        System.out.println(this.toString())
    }
}