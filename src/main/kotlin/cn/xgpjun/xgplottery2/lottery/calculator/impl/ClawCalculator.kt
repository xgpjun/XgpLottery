package cn.xgpjun.xgplottery2.lottery.calculator.impl

import cn.xgpjun.xgplottery2.lottery.calculator.Calculator
import cn.xgpjun.xgplottery2.lottery.pojo.Award
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.DatabaseManager
import org.bukkit.entity.Player

//娃娃机 全服共保底
class ClawCalculator: Calculator() {

    private var nonGuaranteedCount = 0
    override fun getAward(player: Player, lottery: Lottery): Award? {
        val playerData = DatabaseManager.getPlayerData(player.uniqueId)
        playerData.addCount(lottery)
        val guaranteedCount = lottery.getGuaranteedCount()
        nonGuaranteedCount++
        //奖池拥有保底机制
        if (guaranteedCount is Int && guaranteedCount>0 ){
            //满足保底情况
            if (nonGuaranteedCount>=guaranteedCount){
                nonGuaranteedCount = 0
                val guaranteedList = lottery.awards.values.filter { it.isGuaranteed() }
                val totalWeight = guaranteedList.sumOf { it.weight }
                val randomWeight = (1..totalWeight).random()
                var cumulativeWeight = 0
                for (award in guaranteedList){
                    cumulativeWeight += award.weight
                    if (randomWeight <= cumulativeWeight){
                        return award
                    }
                }
            }
        }
        //其他情况、 提前出了
        val award = NormalCalculator.onlyGetAward(lottery)
        if (award?.isGuaranteed() == true){
            nonGuaranteedCount = 0
        }
        return award
    }
}