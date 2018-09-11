package com.heid.games.model.dice

import android.content.Intent
import android.os.Bundle
import com.heid.games.R
import com.heid.games.base.BaseGameActivity

/**
 * @package     com.heid.games.model.dice
 * @author      lucas
 * @date        2018/9/8
 * @version     V1.0
 * @describe    游戏结束
 */
class OverActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int =R.layout.activity_over
    companion object {
        fun launch(activity: BaseGameActivity, resPoint: String?){
            val intent = Intent(activity, OverActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_dice_bg)
        setTitle("吹牛")
        setGameRule("game_rule/chuiniu.html")
    }
}
