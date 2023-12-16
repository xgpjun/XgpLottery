package cn.xgpjun.xgplottery2.lottery.calculator.impl

import cn.xgpjun.xgplottery2.manager.DatabaseManager
import cn.xgpjun.xgplottery2.lottery.calculator.Calculator
import cn.xgpjun.xgplottery2.lottery.pojo.Award
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import org.bukkit.entity.Player

class NormalCalculator:Calculator() {
    override fun getAward(player: Player, lottery: Lottery): Award? {
        val playerData = DatabaseManager.getPlayerData(player.uniqueId)
        playerData.addCount(lottery)
        return onlyGetAward(lottery)
    }

    companion object{
        fun onlyGetAward(lottery: Lottery):Award?{
            //总权重
            val totalWeight = lottery.getTotalWeight()
            //生成一个随机数，范围从 1 到总权重
            val randomWeight = (1..totalWeight).random()
            // 遍历 Awards 并根据权重选择一个 Award
            var cumulativeWeight = 0
            for (award in lottery.awards.values) {
                cumulativeWeight += award.weight
                if (randomWeight <= cumulativeWeight) {
                    return award
                }
            }
            //这怎么可能到达呢= =
            return null
        }
    }
}