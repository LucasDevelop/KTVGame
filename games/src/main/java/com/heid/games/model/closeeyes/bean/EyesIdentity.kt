package com.heid.games.model.closeeyes.bean

import java.io.Serializable

/**
 * @package     com.heid.games.model.closeeyes.bean
 * @author      lucas
 * @date        2018/9/3
 * @des
 */
class EyesIdentity (val index :Int ,val identity:String,val icon:String):Serializable{
    var isAlive = true//是否存活
}