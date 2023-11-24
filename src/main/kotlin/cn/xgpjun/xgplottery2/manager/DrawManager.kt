package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.data.PlayerData
import cn.xgpjun.xgplottery2.lottery.calculator.Calculator
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.utils.Config
import cn.xgpjun.xgplottery2.utils.VersionAdapterUtils
import org.bukkit.entity.Player

object DrawManager {
    val calculators = HashMap<String,Calculator>()
    fun isValid(player: Player, lottery: Lottery,count: Int = 1): Boolean {
        return maxDrawTime(player,lottery,count)
    }

    fun lotteryValid(lottery: Lottery):Boolean{
        return lottery.getTotalWeight() > 0
    }

    fun maxDrawTime(player: Player,lottery: Lottery,count: Int = 1):Boolean{
        lottery.customTags["maxDrawTime"]?.let { maxDrawTime ->
            if (maxDrawTime is Int){
                PlayerData.getPlayerData(player).individualPoolDrawCount[lottery.name]?.let {  time->
                    if (maxDrawTime - time < count){
                        return false
                    }
                }

            }
        }
        return true
    }

    fun checkKeyCount(player: Player,lottery: Lottery,count: Int) =
        PlayerData.getPlayerData(player).keyCount.getOrDefault(lottery.name,0) >= count

    fun checkFullInventory(player: Player,count: Int) =
        Config.checkFullInventory&&VersionAdapterUtils.getPlayerEmptySlot(player) >= count

    fun draw(player: Player,lottery: Lottery,isMultiple:Boolean = false,isConsumeKeys:Boolean =false){

        val amount = if(isMultiple) 10 else 1
        //先确认是否能抽
        if (checkFullInventory(player,amount)&&
            (!isConsumeKeys||checkKeyCount(player,lottery,amount))&&
            isValid(player,lottery,amount) &&
            lotteryValid(lottery)){
            return
        }

        //扣除key
        val playerData = PlayerData.getPlayerData(player)

        if (isConsumeKeys){
            playerData.consumeKey(lottery,amount)
        }

        //开始抽奖
        player.closeInventory()
        if(isMultiple){
            lottery.multipleDraw(player)
        }else{
            lottery.singleDraw(player)
        }
    }

}