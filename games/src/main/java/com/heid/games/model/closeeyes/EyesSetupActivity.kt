package com.heid.games.model.closeeyes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.config.GameConfig
import com.heid.games.model.closeeyes.bean.EyesIdentity
import kotlinx.android.synthetic.main.activity_eyes_setup.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * @package     com.heid.games.model.closeeyes
 * @author      lucas
 * @date        2018/9/3
 * @version     V1.0
 * @describe    确认身份
 */
class EyesSetupActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_eyes_setup
    val count: Int by lazy { intent.getIntExtra("person_count", 0) }
    //身份列表
    val rowIdentitys: ArrayList<String> by lazy {
        val list = ArrayList<String>()
        val split = GameConfig.eyesPersonTable[count]?.split("-")!!
        //杀手人数
        split[0].toInt().repeat { list.add("杀手") }
        //警察人数
        split[1].toInt().repeat { list.add("警察") }
        //平民人数
        split[2].toInt().repeat { list.add("平民") }
        //洗牌
        list.shuffle()
        list
    }
    //确认后的身份
    val comfirmIdentity: ArrayList<EyesIdentity> = ArrayList()

    companion object {
        fun launch(activity: BaseGameActivity, personCount: Int) {
            val intent = Intent(activity, EyesSetupActivity::class.java)
            intent.putExtra("person_count", personCount)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_eyes_bg)
        setTitle("天黑请闭眼")
        v_check.setChangeClickListener { view, b ->
            if (b)
                v_camera.saveImg({
                    //保存前
                    v_progress.show()
                    v_check_text.hide()
                }, { path, isSuccess ->
                    //保存后
                    v_identity_contain.show()
                    v_progress.hide()
                    v_icon.show()
                    v_camera.hide()
                    v_check_text.show()
                    v_num.text = "${comfirmIdentity.size + 1}号"
                    //显示身份
                    val identity = EyesIdentity(comfirmIdentity.size + 1, rowIdentitys[comfirmIdentity.size], path)
                    comfirmIdentity.add(identity)
                    v_num.text = "${identity.index}号"
                    v_identity.text = identity.identity
                    //判断是否结束
                    if (comfirmIdentity.size == count)
                        v_check_text.text = "传给法官"
                    else
                        v_check_text.text = "传给下一个人"
                })
            else {
                //判断是否结束
                if (comfirmIdentity.size == count) {
                    PreparePlayActivity.launch(this, comfirmIdentity)
                    finish()
                    return@setChangeClickListener
                }
                v_identity_contain.hide(false)
                v_icon.hide()
                v_camera.show()
                v_check_text.text = "${comfirmIdentity.size + 1}号查看身份"
            }
        }
    }
}
