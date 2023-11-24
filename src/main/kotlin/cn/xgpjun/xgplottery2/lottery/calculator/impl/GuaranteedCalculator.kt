package cn.xgpjun.xgplottery2.lottery.calculator.impl

import cn.xgpjun.xgplottery2.data.PlayerData
import cn.xgpjun.xgplottery2.lottery.calculator.Calculator
import cn.xgpjun.xgplottery2.lottery.pojo.Award
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import org.bukkit.entity.Player


class GuaranteedCalculator:Calculator() {
    override fun getAward(player: Player, lottery: Lottery): Award? {
        val playerData = PlayerData.getPlayerData(player)
        val nonGuaranteedCount = playerData.customData.getOrDefault("nonGuaranteed${lottery.name}",0) as Int + 1
        playerData.addCount(lottery)
        val guaranteedCount = lottery.getGuaranteedCount()
        //奖池拥有保底机制
        if (guaranteedCount is Int && guaranteedCount>0 ){
            //满足保底情况
            if (nonGuaranteedCount>=guaranteedCount){
                playerData.customData["nonGuaranteed${lottery.name}"] = 0
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
            }else{
                playerData.customData["nonGuaranteed${lottery.name}"] = nonGuaranteedCount
            }
        }
        //其他情况
        return NormalCalculator.onlyGetAward(lottery)
    }

}

fun Lottery.getGuaranteedCount() = customTags.getOrDefault("guaranteedCount",0)


fun Award.isGuaranteed():Boolean{
    val tag = customTag.getOrDefault("guaranteedRewards",false)
    return tag.toString().toBoolean()
}