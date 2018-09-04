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
        //天黑请闭眼游戏流程
        //每当平民死亡可以说一次遗言
        //输赢判定：如果杀手全部出局或者平民和警察都死亡则胜
        internal val eyesRule = hashMapOf(
                0 to "法官说话，天黑请闭眼",
                1 to "杀手选择要杀的人",
                2 to "法官说话，请警察验人",
                3 to "警察选择人验证",
                4 to "法官说话，告诉警察所验证的人是否为杀手",
                5 to "法官说话，平民遗言",
                6 to "众人投票，选择出局人"
        )
    }
}