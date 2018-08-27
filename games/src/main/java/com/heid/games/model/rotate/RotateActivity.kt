package com.heid.games.model.rotate

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import kotlinx.android.synthetic.main.activity_rotate.*

/**
 * @package     com.heid.games.model
 * @author      lucas
 * @date        2018/8/27
 * @version     V1.0
 * @describe    有胆你就转
 */
class RotateActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_rotate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("有胆你就转")
        setBg(R.mipmap.ic_rotate_bg)
        (v_anim.drawable as AnimationDrawable).start()
        v_rotate_view.start()
    }
}
