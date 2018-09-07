package com.heid.games.model.closeeyes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.config.GameConfig
import com.heid.games.model.closeeyes.bean.EyesIdentity
import com.heid.games.model.closeeyes.bean.EyesIdentityBean
import com.heid.games.model.closeeyes.fragment.EyesLinesFragment
import com.heid.games.model.closeeyes.fragment.EyesSelectFragment

/**
 * @package     com.heid.games.model.closeeyes
 * @author      lucas
 * @date        2018/9/3
 * @version     V1.0
 * @describe    开始游戏
 */
class EyesPlayActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_eyes_play
    val person: ArrayList<EyesIdentityBean> by lazy { intent.getSerializableExtra("person") as ArrayList<EyesIdentityBean> }

    val mLinesF = EyesLinesFragment()
    val mSelectF = EyesSelectFragment()

    //游戏当前进度
    var currentProgress = 0
        set(value) {
            field = value % GameConfig.eyesRule.size
            Log.d("lucas", "当前进度：${GameConfig.eyesRule[field]}")
        }
    //最后一次呗杀的人
    var lastKill:EyesIdentityBean?=null

    companion object {
        fun launch(activity: BaseGameActivity, person: ArrayList<EyesIdentityBean>) {
            val intent = Intent(activity, EyesPlayActivity::class.java)
            intent.putExtra("person", person)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_eyes_play_bg)
        setTitle("天黑请闭眼")
        val bundle = Bundle()
        bundle.putSerializable("person", person)
        mLinesF.arguments = bundle
        mSelectF.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.v_fragment, mLinesF)
                .add(R.id.v_fragment, mSelectF).commit()
        showLines()
    }

    override fun onStart() {
        super.onStart()
        mLinesF.init()
        mSelectF.init()
        //1.法官说开始台词
        mLinesF.sayLines()
    }

    fun showLines() {
        supportFragmentManager.beginTransaction().show(mLinesF).hide(mSelectF).commit()
    }

    fun showSelect() {
        supportFragmentManager.beginTransaction().hide(mLinesF).show(mSelectF).commit()
    }

}
