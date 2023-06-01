package cn.xgp.xgplottery.Command.SubCmd;

import cn.xgp.xgplottery.Command.XgpLotteryCommand;
import cn.xgp.xgplottery.Lottery.BoxParticle;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.nmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticleCommand implements TabExecutor {

    /***
     * /xl particle show
     * /xl particle clear
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player &&sender.hasPermission("xgplottery.manager"))){
            sender.sendMessage(ChatColor.RED+ LangUtils.DontHavePermission);
            return true;
        }
        if(Bukkit.getPluginManager().getPlugin("ParticleLib") == null){
            sender.sendMessage(ChatColor.RED+ LangUtils.NotFoundAPI+"ParticleLib");
            return true;
        }
        if(!ConfigSetting.enableParticle){
            sender.sendMessage(ChatColor.RED+LangUtils.NotEnableParticle);
            return true;
        }

        if(nmsUtils.versionToInt<9){
            sender.sendMessage(ChatColor.RED+LangUtils.CantEnableParticle);
            return true;
        }

        if(args.length!=2||(!args[1].equals("show")&&!args[1].equals("clear"))){
            sender.sendMessage(ChatColor.RED+LangUtils.WrongInput);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery particle show\n" + ChatColor.GREEN + LangUtils.CmdParticle1);
            sender.sendMessage(ChatColor.AQUA + "/XgpLottery particle clear\n" + ChatColor.GREEN + LangUtils.CmdParticle2);
            return true;
        }
        if(args[1].equals("show"))
            BoxParticle.playAllParticle();
        else
            BoxParticle.clearAllParticle();

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2){
            return XgpLotteryCommand.filter(new ArrayList<>(Arrays.asList("show", "clear")),args);
        }
        return new ArrayList<>();
    }
}
