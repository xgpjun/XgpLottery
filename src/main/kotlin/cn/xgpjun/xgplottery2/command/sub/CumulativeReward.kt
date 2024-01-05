package cn.xgpjun.xgplottery2.command.sub

import cn.xgpjun.xgplottery2.command.filter
import cn.xgpjun.xgplottery2.lottery.pojo.CumulativeRewardManager
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object CumulativeReward :TabExecutor{
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String>? {
        return when(args.size){
            2 -> CumulativeRewardManager.cumulativeRewardMap.keys.toMutableList().filter(args)
            else -> null
        }
    }

    /**
     * /xl cr name
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player){
            Message.OnlyPlayer.get().send(sender)
            return true
        }
        args.getOrNull(1)?.let {
            CumulativeRewardManager.getCumulativeReward(it)?.give(sender) ?:Message.NotFoundLottery.get().send(sender)
        }
        return true
    }
}