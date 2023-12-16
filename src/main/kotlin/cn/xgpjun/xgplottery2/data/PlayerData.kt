package cn.xgpjun.xgplottery2.data

import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import com.google.gson.Gson
import java.util.UUID

/**
 * uuid: 玩家uuid
 * totalDrawCount: 玩家总抽奖次数
 * individualPoolDrawCount: 单独奖池抽奖次数, key为奖池名称
 * keyCount: 钥匙数量,key为奖池名称
 * customData: ...
 */
class PlayerData(
    var uuid: UUID,
    var totalDrawCount: Int,
    val individualPoolDrawCount:MutableMap<String,Int>,
    val keyCount:MutableMap<String,Int>,
    val customData:MutableMap<String,Any>,
    ) {

    fun addCount(lottery: Lottery){
        totalDrawCount += 1
        individualPoolDrawCount[lottery.name] = individualPoolDrawCount.getOrDefault(lottery.name,0) + 1
    }

    fun consumeKey(lottery: Lottery,count:Int){
        val result = keyCount.getOrDefault(lottery.virtualKeyName,0) - count
        keyCount[lottery.virtualKeyName] = result
    }

    fun individualPoolDrawCountString(): String = Gson().toJson(individualPoolDrawCount)

    fun keyCountString(): String = Gson().toJson(keyCount)

    fun customDataString(): String = Gson().toJson(customData)

}
