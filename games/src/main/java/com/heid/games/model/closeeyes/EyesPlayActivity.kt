package com.heid.games.model.closeeyes

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.model.closeeyes.adapter.EyesKillPersonAdapter
import com.heid.games.model.closeeyes.bean.EyesIdentity
import kotlinx.android.synthetic.main.layout_eyes_lines.*
import kotlinx.android.synthetic.main.layout_eyes_select_person.*

/**
 * @package     com.heid.games.model.closeeyes
 * @author      lucas
 * @date        2018/9/3
 * @version     V1.0
 * @describe    开始游戏
 */
class EyesPlayActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_eyes_play
    val person: ArrayList<EyesIdentity> by lazy { intent.getSerializableExtra("person") as ArrayList<EyesIdentity> }
    val mAdapter = EyesKillPersonAdapter()

    companion object {
        fun launch(activity: BaseGameActivity, person: ArrayList<EyesIdentity>) {
            val intent = Intent(activity, EyesPlayActivity::class.java)
            intent.putExtra("person", person)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_eyes_bg)
        setTitle("天黑请闭眼")
        //法官说开始台词
        sayLines(arrayOf("天黑请闭眼", "杀手请睁眼", "请杀手指定一个要杀死的人"))
        initEvent()
        initSelectKillPerson()
    }

    private fun initSelectKillPerson() {
        v_list.layoutManager = GridLayoutManager(this, Math.ceil(person.size.toDouble() / 4).toInt())
        v_list.adapter = mAdapter
        mAdapter.setNewData(person)
        v_lines_text1.setChangeClickListener { view, b ->
            mAdapter.isShowIdentity = b
        }
        mAdapter.setOnItemLongClickListener { adapter, view, baseViewHolder, position ->
            //杀人,并显示身份
            mAdapter.kill(adapter.getItem(position) as EyesIdentity)
            //法官台词
            sayLines(arrayOf("杀手请闭眼，警察请睁眼", "警察请验人"))
            return@setOnItemLongClickListener true
        }
    }

    private fun initEvent() {
        arrayOf(v_kill).forEach {
            it.setOnClickListener {
                when (it) {
                    v_kill -> {//点击杀人
                        selectKillPerson()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    //选择要杀的人
    fun selectKillPerson() {
        v_layout_select.onlyShowView()
    }

    //法官台词
    fun sayLines(lines: Array<String>, text1: String = "", text2: String = "", text3: String = "", isKill: Boolean = false) {
        v_layout_lines.onlyShowView()
        v_lines_contain.removeAllViews()
        if (text1.isNotEmpty()) {
            v_lines_text1.show()
            v_lines_text1.text = text1
        }
        if (text2.isNotEmpty()) {
            v_lines_text2.show()
            v_lines_text2.text = text2
        }
        if (text3.isNotEmpty()) {
            v_lines_text3.show()
            v_lines_text3.text = text3
        }
        //是否显示杀人
        v_kill.visibility = if (isKill) View.VISIBLE else View.GONE
        lines.forEach {
            val view = LayoutInflater.from(this).inflate(R.layout.view_eyes_lines, v_lines_contain, false) as ViewGroup
            v_lines_contain.addView(view)
            view.findViewById<TextView>(R.id.v_lines_text).text = it
        }
    }

    fun View.onlyShowView() {
        arrayOf(v_layout_lines, v_layout_lines).forEach {
            if (it == this) it.show() else it.hide()
        }
    }
}
