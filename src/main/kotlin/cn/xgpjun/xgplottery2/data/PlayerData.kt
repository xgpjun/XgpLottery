package cn.xgpjun.xgplottery2.data

import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import org.bukkit.entity.Player
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
        totalDrawCount++
        individualPoolDrawCount[lottery.name] = individualPoolDrawCount.getOrDefault(lottery.name,0) + 1
    }

    fun consumeKey(lottery: Lottery,count:Int){
        val result = keyCount.getOrDefault(lottery.name,0) - count
        keyCount[lottery.name] = result
    }

    companion object{
        fun getPlayerData(player: Player):PlayerData{
            return createPlayerData(player)
            TODO()
        }

        private fun createPlayerData(player: Player) = PlayerData(player.uniqueId,0,HashMap(),HashMap(),HashMap())
    }
}
