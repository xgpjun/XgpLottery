package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.command.filter
import cn.xgpjun.xgplottery2.manager.*
import cn.xgpjun.xgplottery2.send
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object Convert:TabExecutor {
    /**
     * /xl convert toCC
     * /xl convert fromCC
     * /xl convert fromLegacy
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): MutableList<String> {
        return when(args.size){
            2 -> mutableListOf("fromlegacy").filter(args)
            else -> arrayListOf()
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.isOp){
            return true
        }
        when(args.getOrNull(1)){
            "fromlegacy" -> {
                if (Bukkit.getPluginManager().isPluginEnabled("XgpLottery")){
                    SchedulerManager.getScheduler().runTaskAsynchronously{
                        Message.Processing.get().send(sender)
                        val folder = File(XgpLottery.instance.dataFolder,"lottery")
                        if (!folder.exists()){
                            folder.mkdirs()
                        }
                        cn.xgp.xgplottery.XgpLottery.lotteryList.values.forEach{
                            val file = File(folder,"XL1_${it.name}.yml")
                            if (!file.exists()){
                                file.createNewFile()
                                val yaml = YamlConfiguration.loadConfiguration(file)
                                yaml.set("animation","Default")
                                yaml.set("multipleAnimation","DefaultMultiple")
                                yaml.set("calculator","Guaranteed")
                                yaml.set("value",it.value)
                                yaml.set("sellType",it.sellType.name)
                                yaml.set("virtualKeyName",it.name)
                                yaml.set("customTag.guaranteedCount",it.maxTime)
                                yaml.set("customTag.maxDrawCount",it.limitedTimes)
                                yaml.set("key.internal",it.keyItemStack.toNBTString())

                                it.awards.forEachIndexed{index,award ->
                                    yaml.set("awards.award$index.item.internal",award.item.toNBTString())
                                    yaml.set("awards.award$index.giveItem",award.isGiveItem)
                                    yaml.set("awards.award$index.rawItem",award.isShowRaw)
                                    yaml.set("awards.award$index.broadcast",award.isBroadCast)
                                    yaml.set("awards.award$index.weight",award.weight)
                                    yaml.set("awards.award$index.command",award.commands)
                                    yaml.set("awards.award$index.customTag.guaranteedRewards",false)
                                }
                                it.spAwards.forEachIndexed{index,award ->
                                    yaml.set("awards.spAwards$index.item.internal",award.item.toNBTString())
                                    yaml.set("awards.spAwards$index.giveItem",award.isGiveItem)
                                    yaml.set("awards.spAwards$index.rawItem",award.isShowRaw)
                                    yaml.set("awards.spAwards$index.broadcast",award.isBroadCast)
                                    yaml.set("awards.spAwards$index.weight",award.weight)
                                    yaml.set("awards.spAwards$index.command",award.commands)
                                    yaml.set("awards.spAwards$index.customTag.guaranteedRewards",true)
                                }
                                yaml.save(file)
                            }
                        }
                        Message.Success.get().send(sender)
                    }
                }
            }
            else -> help(sender)
        }
        return true
    }

    fun help(sender: CommandSender){
        if (!sender.isOp){
            return
        }
        MessageL.ConvertHelp.get().send(sender)
    }
}