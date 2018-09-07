package com.heid.game

import com.heid.games.model.closeeyes.bean.EyesIdentity
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//        val s = "喝一口"
//        s.substring(0,2).p()
//        s.substring(2,s.length).p()
        val list = arrayListOf(
                EyesIdentity(1, "杀手", "/storage/emulated/0/1536028922019.jpg"),
                EyesIdentity(2, "警察", "/storage/emulated/0/1536028922019.jpg"),
                EyesIdentity(3, "平民", "/storage/emulated/0/1536028922019.jpg"),
                EyesIdentity(4, "平民", "/storage/emulated/0/1536028922019.jpg"),
                EyesIdentity(5, "平民", "/storage/emulated/0/1536028922019.jpg")
        )
        list.forEach {
            Thread.sleep(1)
            it.isAlive=false
        }
        list.p()
        list.maxWith(Comparator{p0,p1-> (p0.killTime-p1.killTime).toInt()})?.p()
        list.shuffle()
        list.p()
        list.maxWith(Comparator{p0,p1-> (p0.killTime-p1.killTime).toInt()})?.p()
    }
    fun Any.p(){
        System.out.println(this.toString())
    }
}
