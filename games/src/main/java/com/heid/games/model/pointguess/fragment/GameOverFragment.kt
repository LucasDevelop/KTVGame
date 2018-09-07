package com.heid.games.model.pointguess.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.heid.games.R
import com.heid.games.model.pointguess.PlayGameActivity
import com.heid.games.model.pointguess.bean.AnswerBean
import kotlinx.android.synthetic.main.fragment_pg_gameover.*

/**
 * @package     com.heid.games.model.pointguess.fragment_pg_gameover
 * @author      lucas
 * @date        2018/9/6
 * @des   游戏结束
 */
class GameOverFragment : Fragment() {
    val inflate: ViewGroup by lazy { LayoutInflater.from(context).inflate(R.layout.fragment_pg_gameover, null, false) as ViewGroup }
    val time: Int by lazy { (activity as PlayGameActivity).time }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflate
    }

    fun refreshView(data: ArrayList<AnswerBean>) {
        v_play_again.setOnClickListener { PlayGameActivity.launch(activity as PlayGameActivity, time) }
        v_back_home.setOnClickListener { activity?.finish() }
        v_answer_list.removeAllViews()
        data.forEach {
            val frameLayout = LayoutInflater.from(context).inflate(R.layout.item_game_over, v_answer_list, false) as FrameLayout
            frameLayout.findViewById<TextView>(R.id.v_text).text = it.key
            frameLayout.findViewById<View>(R.id.v_icon).isEnabled = it.isRight
            v_answer_list.addView(frameLayout)
        }
        val size = data.filter { it.isRight }.size
        v_title.text = "您总共答对${size}题"
        var isHigh = false
        var isTop = false
        when (time) {
            60 -> {
                isHigh = size >= 5
                isTop = size >= 9
            }
            90 -> {
                isTop = size >= 12
                isHigh = size >= 6
            }
            120 -> {
                isTop = size >= 15
                isHigh = size >= 8
            }
        }
        v_pg_gameover_bg.setImageResource(if (isHigh) R.mipmap.ic_pg_score_higt else R.mipmap.ic_pg_score_low)
        if (isTop) v_pg_gameover_bg.setImageResource(R.mipmap.ic_pg_score_top)
    }
}