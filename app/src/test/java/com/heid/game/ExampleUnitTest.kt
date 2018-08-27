package com.heid.game

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
        val s = "喝一口"
        s.substring(0,2).p()
        s.substring(2,s.length).p()

    }
    fun Any.p(){
        System.out.println(this)
    }
}
