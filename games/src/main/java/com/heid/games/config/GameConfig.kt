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
        internal val CROWD_NUM_COUNT = 8*12//格子个数
    }
}