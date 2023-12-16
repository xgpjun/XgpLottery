package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.lottery.calculator.Calculator
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.send
import cn.xgpjun.xgplottery2.utils.Config
import cn.xgpjun.xgplottery2.utils.VersionAdapterUtils
import org.bukkit.entity.Player

object DrawManager {
    val calculators = HashMap<String,Calculator>()


    fun isValid(player: Player, lottery: Lottery,count: Int = 1): Boolean {
        return maxDrawTime(player,lottery,count)
    }

    fun lotteryValid(player: Player,lottery: Lottery):Boolean{
        val result = lottery.getTotalWeight() > 0
        if (!result){
            Message.TotalWeightNegative.get().send(player)
        }
        return result
    }

    /**
     * @return 玩家抽取数量未达限制时为true
     */
    private fun maxDrawTime(player: Player, lottery: Lottery, count: Int = 1):Boolean{
        lottery.customTags["maxDrawTime"]?.let { maxDrawTime ->
            if (maxDrawTime is Int){
                DatabaseManager.getPlayerData(player.uniqueId).individualPoolDrawCount[lottery.name]?.let { time->
                    if (maxDrawTime - time < count){
                        Message.MaxTimeLimit.get(count.toString(),time.toString(),maxDrawTime.toString()).send(player)
                        return false
                    }
                }
            }
        }
        return true
    }

    /**
     * @return 不满足条件返回false
     */
    fun checkKeyCount(player: Player,lottery: Lottery,count: Int):Boolean{
        val result = DatabaseManager.getPlayerData(player.uniqueId).keyCount.getOrDefault(lottery.virtualKeyName,0) >= count
        if (!result){
            Message.NoEnoughKeys.get(lottery.virtualKeyName).send(player)
        }
        return result
    }


    /**
     * @return 不满足条件返回false
     */
    fun checkFullInventory(player: Player,count: Int) :Boolean{
        val result = !Config.checkFullInventory||VersionAdapterUtils.getPlayerEmptySlot(player) >= count
        if (!result){
            Message.InvFull.get().send(player)
        }
        return result
    }


    fun draw(player: Player,lottery: Lottery,isMultiple:Boolean = false,isConsumeKeys:Boolean =false){

        val amount = if(isMultiple) 10 else 1
        //先确认是否能抽
        if (!checkFullInventory(player,amount)||
            (isConsumeKeys&&!checkKeyCount(player,lottery,amount))||
            !isValid(player,lottery,amount) ||
            !lotteryValid(player,lottery)){
            return
        }

        //扣除key
        val playerData = DatabaseManager.getPlayerData(player.uniqueId)

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