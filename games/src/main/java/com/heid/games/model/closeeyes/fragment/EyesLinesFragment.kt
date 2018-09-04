package com.heid.games.model.closeeyes.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.heid.games.R
import com.heid.games.model.closeeyes.EyesPlayActivity
import com.heid.games.model.closeeyes.bean.EyesIdentity
import com.heid.games.utils.ViewUtil
import kotlinx.android.synthetic.main.layout_eyes_lines.*

/**
 * @package     com.heid.games.model.closeeyes.fragment
 * @author      lucas
 * @date        2018/9/4
 * @des
 */
class EyesLinesFragment : Fragment(), ViewUtil {
    val person: ArrayList<EyesIdentity> by lazy { arguments!!.getSerializable("person") as ArrayList<EyesIdentity> }
    val mActivity: EyesPlayActivity by lazy { activity as EyesPlayActivity }
    lateinit var rootView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_eyes_lines, null)
        return rootView
    }

    fun init() {
        initEvent()
    }

    private fun initEvent() {
        v_kill.setOnClickListener {
            mActivity.currentProgress++
            mActivity.showSelect()
            mActivity.mSelectF.refreshView()
        }
        //验人
        v_check_person.setOnClickListener {
            mActivity.currentProgress++
            mActivity.showSelect()
            mActivity.mSelectF.refreshView()
        }
        v_lines_continue.setOnClickListener {
            mActivity.currentProgress++
            mActivity.showLines()
            mActivity.mLinesF.sayLines()
        }
        //投票
        v_lines_vote_bt.setOnClickListener {
            mActivity.currentProgress++
            mActivity.showSelect()
            mActivity.mSelectF.refreshView()
        }
    }

    //法官台词
    fun sayLines() {
        var lines: Array<String> = arrayOf()
        v_lines_contain.removeAllViews()
        if (mActivity.currentProgress == 0) {
            //是否显示杀人
            v_kill.onlyShowBt()
            lines = arrayOf("天黑请闭眼", "杀手请睁眼", "请杀手指定一个要杀死的人")
        }
        if (mActivity.currentProgress == 2) {
            //是否显示验人
            v_check_person.onlyShowBt()
            lines = arrayOf("杀手请闭眼，警察请睁眼", "警察请验人")
        }
        if (mActivity.currentProgress == 4) {
            v_hint_kill.onlyShowBt()
            v_lines_continue.show()
            lines = arrayOf("警察请闭眼\n天亮了，请所有人睁眼")
        }
        if (mActivity.currentProgress == 5) {
            v_lines_vote_bt.onlyShowBt()
            v_lines_peace_day.show()
            lines = arrayOf("x号是平民，有遗言","从死者左边第一个人\n开始发言")
        }

        lines.forEach {
            val view = LayoutInflater.from(context).inflate(R.layout.view_eyes_lines, v_lines_contain, false) as ViewGroup
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.CENTER
            v_lines_contain.addView(view)
            view.findViewById<TextView>(R.id.v_lines_text).text = it
        }
    }

    fun View.onlyShowBt() {
        arrayOf(v_kill, v_check_person, v_show_identity, v_lines_complete, v_hint_kill, v_lines_continue,v_lines_vote_bt,v_lines_peace_day).forEach {
            it.visibility = if (this == it) View.VISIBLE else View.GONE
        }
    }
}