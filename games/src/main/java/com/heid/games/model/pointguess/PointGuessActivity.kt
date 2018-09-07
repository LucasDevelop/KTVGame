package com.heid.games.model.pointguess

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import kotlinx.android.synthetic.main.activity_point_guess.*

/**
 * @package     com.heid.games.model.pointguess
 * @author      lucas
 * @date        2018/9/5
 * @version     V1.0
 * @describe    你画我猜
 */
class PointGuessActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_point_guess

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_pg_bg)
        setTitle("你画我猜")
        setBackBg(R.drawable.shape_back_y_bg)
        v_start.setOnClickListener {
            val buttonId = v_select_time.checkedRadioButtonId
            val toInt = findViewById<RadioButton>(buttonId).text.toString().replace("s", "").toInt()
            PlayGameActivity.launch(this,toInt)
        }
    }
}
