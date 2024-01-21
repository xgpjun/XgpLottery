package cn.xgpjun.xgplottery2.lottery.calculator.impl

import cn.xgpjun.xgplottery2.lottery.calculator.Calculator
import cn.xgpjun.xgplottery2.lottery.pojo.Award
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.DatabaseManager
import org.bukkit.entity.Player

class AbsoluteGuarantee: Calculator() {
    //不同的是，这个计算器只有绝对满足抽奖次数的时候才会获得保底物品
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
        //其他情况 随机从普通物品里面获得一个
        val noGuaranteedList = lottery.awards.values.filter { !it.isGuaranteed() }
        return noGuaranteedList.getAward()
    }
}