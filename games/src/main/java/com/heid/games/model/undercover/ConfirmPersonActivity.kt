package com.heid.games.model.undercover

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.model.undercover.adapter.UnderPersonAdapter
import com.heid.games.model.undercover.bean.PersonInfoBean
import kotlinx.android.synthetic.main.activity_confirm_person.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * @package     com.heid.games.model.undercover
 * @author      lucas
 * @date        2018/8/31
 * @version     V1.0
 * @describe    人员列表
 */

class ConfirmPersonActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_confirm_person
    val persons: ArrayList<PersonInfoBean> by lazy { intent.getSerializableExtra("persons") as ArrayList<PersonInfoBean> }
    val mAdapter = UnderPersonAdapter()
    val random = Random()
    //未出局的人--用于随机定位
    val unOutPerson = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_under_setup_bg)
        setTitle("谁是卧底")
        unOutPerson.addAll(Array(persons.size, { it }).toList())
        v_list.layoutManager = GridLayoutManager(this, 4)
        v_list.adapter = mAdapter
        mAdapter.setNewData(persons)
        initForgetWord()
        //随机开始发言
        speek()
    }

    private fun speek() {
        val nextInt = random.nextInt(unOutPerson.size - 1)
        v_hint_text.text = "${unOutPerson[nextInt] + 1}号开始发言，所有人发言后再投票"
        mAdapter.isShowPosition = unOutPerson[nextInt]
    }

    private fun initForgetWord() {
        mAdapter.setOnItemLongClickListener { adapter, view, baseViewHolder, position ->
            //显示投票结果--玩家出局
            if (position !in mAdapter.votes) {
                persons[position].isLife = false
                mAdapter.votes.add(position)
                mAdapter.notifyDataSetChanged()
                unOutPerson.remove(position)
                //随机选择下一位发言者
                speek()
            }
            //判断游戏结束
            var underCount = 0//存活卧底数
            var commonCount = 0//存活平民数
            unOutPerson.forEach {
                if (persons[it].isUnder)
                    underCount++
                else
                    commonCount++
            }
            //卧底全部出局则是平民胜
            if (underCount == 0) {
                VictoryActivity.launch(this, 1, persons)
                return@setOnItemLongClickListener true
            }
            //平民全部出局则是卧底胜
            if (commonCount == 0) {
                VictoryActivity.launch(this, 0, persons)
                return@setOnItemLongClickListener true
            }
            //如果剩下一个卧底和一个平民则是卧底胜
            if (underCount == 1 && commonCount == 1) {
                VictoryActivity.launch(this, 0, persons)
            }
            return@setOnItemLongClickListener true
        }
        mAdapter.setOnItemClickListener { adapter, view, baseViewHolder, position ->
            //显示忘词
            if (mAdapter.isShowHint) {
                v_start_view.visibility = View.VISIBLE
                v_words.text = (adapter.getItem(position) as PersonInfoBean).identity
                v_start_view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.under_hint_show))
                mHandler.postDelayed({
                    val animation = AnimationUtils.loadAnimation(this, R.anim.under_hint_hide)
                    v_start_view.startAnimation(animation)
                    animation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                        override fun onAnimationEnd(p0: Animation?) {
                            v_start_view.visibility = View.GONE
                        }

                        override fun onAnimationStart(p0: Animation?) {
                        }
                    })
                }, 2000)
            }else{
                "长按投票".toast(this)
            }

        }

        v_confirm.setOnClickListener {
            if (!mAdapter.isShowHint) {
                mAdapter.isShowHint = true
                v_confirm_img.setImageResource(R.mipmap.ic_cancel)
            } else {
                mAdapter.isShowHint = false
                v_confirm_img.setImageResource(R.mipmap.ic_under_wc)
            }
        }
    }
}
