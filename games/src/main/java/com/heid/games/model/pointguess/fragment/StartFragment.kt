package com.heid.games.model.pointguess.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heid.games.R
import com.heid.games.model.pointguess.PlayGameActivity

/**
 * @package     com.heid.games.model.pointguess.fragment_pg_gameover
 * @author      lucas
 * @date        2018/9/6
 * @des    开始游戏
 */
class StartFragment : Fragment() {
    val mActivity:PlayGameActivity by lazy { activity as PlayGameActivity }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = LayoutInflater.from(context).inflate(R.layout.fragment_pg_start, null, false)!!
        initView(inflate)
        return inflate
    }

    private fun initView(inflate: View) {
        inflate.findViewById<View>(R.id.v_close).setOnClickListener { mActivity.finish() }
        inflate.findViewById<View>(R.id.v_start).setOnClickListener { mActivity.nextPage() }
    }
}