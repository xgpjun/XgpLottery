package cn.xgpjun.xgplottery2.lottery.pojo

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.log
import cn.xgpjun.xgplottery2.manager.*
import cn.xgpjun.xgplottery2.send
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.*
import kotlin.collections.HashMap

data class CumulativeReward(
    val name : String,
    val lottery : String,
    val baseTimes:Int,
    val maximum:Int,
    val awards:MutableMap<String,Award>
) {
    fun give(player: Player){
        val playerData = DatabaseManager.getPlayerData(player.uniqueId)
        val receiveTimes = playerData.customData.getOrDefault("reciveTimes$name",0).toString().toInt()
        val individualTimes = playerData.individualPoolDrawCount.getOrDefault(lottery,0)
        if (receiveTimes>=maximum){
            Message.ReceiveMaximum.get().send(player)
            return
        }
        if (individualTimes - ((receiveTimes+1)*baseTimes)<0){
            Message.ReceiveRewardFailed.get((((receiveTimes+1)*baseTimes) - individualTimes).toString()).send(player)
            return
        }

        playerData.customData["reciveTimes$name"] = receiveTimes + 1
        DatabaseManager.onlinePlayerData[player.uniqueId] = playerData
        Message.Success.get().send(player)
        awards.forEach{
            it.value.give(player)
        }
    }
}

object CumulativeRewardManager{
    val cumulativeRewardMap = HashMap<String,CumulativeReward>()

    fun register(){
        val folder = File(XgpLottery.instance.dataFolder,"cumulativeReward")
        if (!folder.exists()) {
            return
        }
        val yamlFiles: Array<out File>? = folder.listFiles { _:File, name:String ->
            name.lowercase(Locale.getDefault()).endsWith(".yml")
        }
        yamlFiles?.forEach { file: File ->
            val yaml = YamlConfiguration.loadConfiguration(file)
            val name = file.name.replace(".yml","")
            val lotteryName = yaml.getString("lotteryName")?:run { Message.MissingNode.get(file.name,"lotteryName").log();return@forEach }
            val baseTimes = yaml.getInt("baseTimes")?:0
            if (baseTimes==0){
                Message.MissingNode.get(file.name,"animation").log() ;return@forEach
            }
            val maximum = yaml.getInt("maximum")?:0


            val awards = LotteryManager.getAwards(yaml)
            cumulativeRewardMap[name] = CumulativeReward(name,lotteryName,baseTimes,maximum,awards)
        }
    }

    fun getCumulativeReward(name: String) = cumulativeRewardMap[name]
}