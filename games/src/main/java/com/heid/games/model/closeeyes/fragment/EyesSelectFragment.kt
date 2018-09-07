package com.heid.games.model.closeeyes.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.heid.games.R
import com.heid.games.model.closeeyes.EyesPlayActivity
import com.heid.games.model.closeeyes.EyesWinActivity
import com.heid.games.model.closeeyes.adapter.EyesKillPersonAdapter
import com.heid.games.model.closeeyes.bean.EyesIdentity
import com.heid.games.model.closeeyes.bean.EyesIdentityBean
import com.heid.games.utils.ViewUtil
import kotlinx.android.synthetic.main.layout_eyes_select_person.*

/**
 * @package     com.heid.games.model.closeeyes.fragment_pg_gameover
 * @author      lucas
 * @date        2018/9/4
 * @des
 */
class EyesSelectFragment : Fragment(), ViewUtil {
    val person: ArrayList<EyesIdentityBean> by lazy { (activity as EyesPlayActivity).person }
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
        mAdapter.addNewData(person)
        v_select_show_identity.setonPressListener({ view, isPress ->
            mAdapter.isShowIdentity = isPress
        })
        v_select_complete.setOnClickListener {
            if (mActivity.currentProgress == 3) {
                mAdapter.checkIndex = -1
                //法官台词
                mActivity.currentProgress++
                mActivity.showLines()
                mActivity.mLinesF.sayLines()
            }
            if (mActivity.currentProgress == 6) {
                //杀掉被投票的人
                val identity = mAdapter.data.find { it.index == mAdapter.vote }
                mActivity.lastKill = identity
                identity?.isAlive = false
                mAdapter.vote = -1
                checkGameIsOver()
                //法官台词
                mActivity.currentProgress++
                mActivity.showLines()
                mActivity.mLinesF.sayLines()
            }
        }
        v_hint.setOnClickListener {

        }
        mAdapter.setOnItemClickListener { adapter, view, baseViewHolder, position ->
            val bean = adapter.getItem(position) as EyesIdentity
            //验人,并显示身份
            if (mActivity.currentProgress == 3) {
                mAdapter.checkIndex = bean.index
            }
            //投票,并显示身份
            if (mActivity.currentProgress == 6) {
                mAdapter.vote = bean.index
            }
        }
        mAdapter.setOnItemLongClickListener { adapter, view, baseViewHolder, position ->
            val bean = adapter.getItem(position) as EyesIdentity
            if (bean.identity=="杀手"){
                "杀手不能杀自己或者队友".toast()
                return@setOnItemLongClickListener true
            }
            //杀人,并显示身份
            if (mActivity.currentProgress == 1) {
                mActivity.lastKill = bean
                bean.isAlive = false
                checkGameIsOver()
                mAdapter.notifyDataSetChanged()
                //法官台词
                mActivity.currentProgress++
                mActivity.showLines()
                mActivity.mLinesF.sayLines()
            }
            return@setOnItemLongClickListener true
        }
    }

    //判断游戏是否已经结束
    private fun checkGameIsOver() {
        //判断警察和平民是否全部死亡
        var isLifeKill = false//是否有杀手存活
        var isLifeCommon = false//是否有平民存活
        mAdapter.data.forEach {
            if (it.isAlive && (it.identity == "警察" || it.identity == "平民")) {
                isLifeCommon = true
                return@forEach
            }
        }
        mAdapter.data.forEach {
            if (it.isAlive && it.identity == "杀手") {
                isLifeKill = true
                return@forEach
            }
        }
        //如果两种都存活，继续游戏
        if (isLifeCommon && isLifeKill)
            return
        //数据类型转换
        val list = ArrayList<EyesIdentityBean>()
        person.forEach {
            list.add(EyesIdentityBean(it.index,it.identity,it.icon))
        }
        //如果只有杀手存活，则杀手胜利
        if (isLifeKill) EyesWinActivity.launch(mActivity, list, "杀手")
        //如果杀手全部死亡，则好人胜利
        if (isLifeCommon) EyesWinActivity.launch(mActivity, list, "好人")
        mActivity.finish()
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

    fun String.toast(){
        Toast.makeText(mActivity,this,Toast.LENGTH_SHORT).show()
    }
}