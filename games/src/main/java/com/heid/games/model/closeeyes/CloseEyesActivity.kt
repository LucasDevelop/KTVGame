package com.heid.games.model.closeeyes

import android.os.Bundle
import com.heid.games.R
import com.heid.games.base.BaseGameActivity

/**
 * @package     com.heid.games.model.closeeyes
 * @author      lucas
 * @date        2018/9/1
 * @version     V1.0
 * @describe    天黑请闭眼
 */
class CloseEyesActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_close_eyes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_eyes_bg)
        setTitle("天黑请闭眼")
    }
}
