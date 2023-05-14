package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryMenuGui;
import cn.xgp.xgplottery.Lottery.BoxParticle;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ParticleCommand implements CommandExecutor {

    /***
     * /xl particle show
     * /xl particle clear
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player &&sender.hasPermission("xgplottery.manager"))){
            sender.sendMessage(ChatColor.RED+"你没有权限这么做！");
            return true;
        }
        if(!ConfigSetting.enableParticle){
            sender.sendMessage(ChatColor.RED+"本服未开启粒子特效，请在config.yml中更改”enableParticle“");
            return true;
        }
        String option = args[1];
        if(args.length!=2||(!option.equals("show")&&!option.equals("clear"))){
            sender.sendMessage(ChatColor.RED+"输入格式有误");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery particle show\n" + ChatColor.GREEN + "启用所有粒子特效");
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery particle clear\n" + ChatColor.GREEN + "关闭所有粒子特效");
            return true;
        }

        if(option.equals("show"))
            BoxParticle.playAllParticle();
        else
            BoxParticle.clearAllParticle();

        return true;
    }
}
