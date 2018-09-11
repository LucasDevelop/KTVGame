package com.heid.games.socket

import android.util.Log
import com.google.gson.Gson

/**
 * @file       RequestWrapper.ktt
 * @brief      将请求体简化成DSL结构
 * @author     lucas
 * @date       2018/5/4 0004
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class RequestWrapper<T> : IWrapper() {

    //成功回调
    var _onSuccess: (data: T) -> Unit = {}

    //失败回调
    var _onFail: (e: Exception) -> Unit = {}

    //json解析
    var _parse: (String) -> Unit = {}

    inline fun <reified D> tcp(init: RequestWrapper<D>.() -> Unit): RequestWrapper<D> {
        val wrapper = RequestWrapper<D>()
        wrapper.init()
        wrapper._parse = {
            try {
                val fromJson = Gson().fromJson<D>(it, D::class.java)
                if (fromJson != null)
                    wrapper._onSuccess(fromJson)
                else {
                    Log.e("lucas", "json解析失败 Bean:$fromJson")
                    wrapper._onFail(Exception("json解析失败 Bean:$fromJson"))
                }
            } catch (e: Exception) {
                Log.e("lucas", "json解析失败${e.message}")
                wrapper._onFail(e)
            }
        }
        return wrapper
    }

}