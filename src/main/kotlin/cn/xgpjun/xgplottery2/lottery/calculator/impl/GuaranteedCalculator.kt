package cn.xgpjun.xgplottery2.lottery.calculator.impl

import cn.xgpjun.xgplottery2.log
import cn.xgpjun.xgplottery2.manager.DatabaseManager
import cn.xgpjun.xgplottery2.lottery.calculator.Calculator
import cn.xgpjun.xgplottery2.lottery.pojo.Award
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import org.bukkit.entity.Player


class GuaranteedCalculator:Calculator() {
    override fun getAward(player: Player, lottery: Lottery): Award? {
        val playerData = DatabaseManager.getPlayerData(player.uniqueId)
        val nonGuaranteedCount = playerData.customData.getOrDefault("nonGuaranteed${lottery.name}",0).int() + 1
        playerData.addCount(lottery)
        val guaranteedCount = lottery.getGuaranteedCount()
        //奖池拥有保底机制
        if (guaranteedCount is Int && guaranteedCount>0 ){
            //满足保底情况
            if (nonGuaranteedCount>=guaranteedCount){
                playerData.customData["nonGuaranteed${lottery.name}"] = 0
                val guaranteedList = lottery.awards.values.filter { it.isGuaranteed() }
                return guaranteedList.getAward()
            }else{
                playerData.customData["nonGuaranteed${lottery.name}"] = nonGuaranteedCount
            }
        }
        //其他情况、 提前出了
        val award = NormalCalculator.onlyGetAward(lottery)
        if (award?.isGuaranteed() == true){
            playerData.customData["nonGuaranteed${lottery.name}"] = 0
        }
        return award
    }

}

fun Lottery.getGuaranteedCount() = customTags.getOrDefault("guaranteedCount",0)


fun Award.isGuaranteed():Boolean{
    val tag = customTag.getOrDefault("guaranteedRewards",false)
    return tag.toString().toBoolean()
}

fun Any.int():Int{
    return if (this is Number){
        this.toInt()
    } else{
        "&cNon-numeric error, automatically converted to 0".log()
        0
    }
}