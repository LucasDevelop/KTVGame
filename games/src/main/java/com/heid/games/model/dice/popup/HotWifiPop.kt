package com.heid.games.model.dice.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.PopupWindow
import com.heid.games.R
import com.heid.games.utils.FuncUtil

/**
 * @package     com.heid.games.model.dice.popup
 * @author      lucas
 * @date        2018/9/8
 * @des         创建热点
 */
class HotWifiPop(context: Context) : PopupWindow(context) ,FuncUtil{
    val inflate: ViewGroup by lazy { LayoutInflater.from(context).inflate(R.layout.pop_dice_hot, null, false) as ViewGroup }
    var onCreateHotWifi: (name: String, pwd: String) -> Unit = { name, pwd -> }

    init {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        contentView = inflate
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isFocusable = true
        inputMethodMode = INPUT_METHOD_NEEDED
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

        arrayOf(R.id.v_close, R.id.v_confirm).forEach {
            it.findView<View>().setOnClickListener {
                when (it.id) {
                    R.id.v_close -> dismiss()
                    R.id.v_confirm -> {
                        val name = R.id.v_wifi_name.findView<EditText>().text.toString().trim()
                        val pwd = R.id.v_wifi_pwd.findView<EditText>().text.toString().trim()
                        if (name.isEmpty()){
                            "热点名称不能为空!".toast(context)
                            return@setOnClickListener
                        }
                        if (pwd.isEmpty()){
                            "密码不能为空".toast(context)
                            return@setOnClickListener
                        }
                        if (pwd.length<8){
                            "密码字符数不能少于8个".toast(context)
                            return@setOnClickListener
                        }
                        onCreateHotWifi(name,pwd)
                    }
                }
            }
        }
    }

    fun <T : View> Int.findView(): T {
        return inflate.findViewById<T>(this)
    }
}