package com.heid.games.model.closeeyes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.model.closeeyes.adapter.EyesPersonAdapter
import com.heid.games.model.closeeyes.bean.EyesIdentity
import com.heid.games.model.closeeyes.bean.EyesIdentityBean
import kotlinx.android.synthetic.main.activity_prepare_play.*

/**
 * @package     com.heid.games.model.closeeyes
 * @author      lucas
 * @date        2018/9/3
 * @version     V1.0
 * @describe    准备开始游戏
 */
class PreparePlayActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_prepare_play
    val mAdapter = EyesPersonAdapter()
    val person: ArrayList<EyesIdentityBean> by lazy { intent.getSerializableExtra("person") as ArrayList<EyesIdentityBean> }

    companion object {
        fun launch(activity: BaseGameActivity, person: ArrayList<EyesIdentityBean>) {
            val intent = Intent(activity, PreparePlayActivity::class.java)
            intent.putExtra("person", person)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_eyes_bg)
        setTitle("天黑请闭眼")
        v_list.layoutManager = GridLayoutManager(this,Math.ceil(person.size.toDouble() / 4).toInt())
        v_list.adapter= mAdapter
        mAdapter.addNewData(person)
        v_show_identity.setChangeClickListener { view, b ->
            mAdapter.isShowIdentity = b
        }
        v_start.setOnClickListener {
            //开始游戏
            EyesPlayActivity.launch(this,person)
            finish()
        }
    }
}
