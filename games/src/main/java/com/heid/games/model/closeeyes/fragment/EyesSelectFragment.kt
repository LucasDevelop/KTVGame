package com.heid.games.model.closeeyes.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heid.games.R
import com.heid.games.model.closeeyes.EyesPlayActivity
import com.heid.games.model.closeeyes.adapter.EyesKillPersonAdapter
import com.heid.games.model.closeeyes.bean.EyesIdentity
import com.heid.games.utils.ViewUtil
import kotlinx.android.synthetic.main.layout_eyes_select_person.*

/**
 * @package     com.heid.games.model.closeeyes.fragment
 * @author      lucas
 * @date        2018/9/4
 * @des
 */
class EyesSelectFragment : Fragment(), ViewUtil {
    val person: ArrayList<EyesIdentity> by lazy { arguments!!.getSerializable("person") as ArrayList<EyesIdentity> }
    val mAdapter = EyesKillPersonAdapter()
    val mActivity: EyesPlayActivity by lazy { activity as EyesPlayActivity }
    val mHandler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_eyes_select_person, null)
        return view
    }

    fun init() {
        v_list.layoutManager = GridLayoutManager(context, Math.ceil(person.size.toDouble() / 4).toInt())
        v_list.adapter = mAdapter
        mAdapter.setNewData(person)
        v_select_show_identity.setonPressListener({ view, isPress ->
            mAdapter.isShowIdentity = isPress
        })
        mAdapter.setOnItemLongClickListener { adapter, view, baseViewHolder, position ->
            val bean = adapter.getItem(position) as EyesIdentity
            //杀人,并显示身份
            if (mActivity.currentProgress == 1) {
                bean.isAlive = false
            }
            //验人,并显示身份
            if (mActivity.currentProgress == 3) {
                mAdapter.checkIndex = bean.index
            }
                mAdapter.notifyDataSetChanged()
                mHandler.postDelayed({
                    //法官台词
                    mActivity.currentProgress++
                    mActivity.showLines()
                    mActivity.mLinesF.sayLines()
                }, 2000)
            return@setOnItemLongClickListener true
        }
    }

    fun refreshView() {
        //杀人
        if (mActivity.currentProgress == 1) {
            v_select_show_identity.onlyShow()
            v_hint.text = "长按要杀的人"
        }
        //验人
        if (mActivity.currentProgress == 3) {
            v_select_show_identity.visibility = View.VISIBLE
            v_select_complete.visibility = View.VISIBLE
            v_hint.text = "点击选择要验的人"
        }
        //投票
        if (mActivity.currentProgress == 6) {

            v_hint.text = "点击选择要投票的人"
        }
    }


    fun View.onlyShow() {
        arrayOf(v_select_show_identity, v_select_complete).forEach {
            it.visibility = if (it == this) View.VISIBLE else View.GONE
        }
    }
}