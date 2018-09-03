package com.heid.games.config

/**
 * @package     com.heid.games.config
 * @author      lucas
 * @date        2018/8/24
 * @des         游戏配置
 */
abstract class GameConfig {
    companion object {
        //大家来抽签
        internal val DRAW_BT_COUNT = 20//格子个数
        // 疯狂挤数字
        internal val CROWD_NUM_COUNT = 8 * 12//格子个数
        //卧底游戏的参与人数与法官人数映射表
        internal val underPersonTable = hashMapOf(4 to 1, 5 to 1, 6 to 2, 7 to 2, 8 to 2, 9 to 3, 10 to 3, 11 to 3, 12 to 4)
        //天黑请闭眼人数
        internal val eyesPersonTable = hashMapOf(5 to "1-1-3", 6 to "1-1-4", 7 to "1-1-5", 8 to "2-2-4", 9 to "2-2-5", 10 to "2-2-6",
                11 to "3-3-5", 12 to "3-3-5", 13 to "3-3-7", 14 to "3-3-8", 15 to "4-4-7", 16 to "4-4-8")
    }
}