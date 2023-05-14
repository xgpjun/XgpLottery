package cn.xgp.xgplottery.Command;

import cn.xgp.xgplottery.Command.SubCmd.*;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class XgpLotteryCommand implements CommandExecutor{
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();

    public XgpLotteryCommand(){
        registerSubCommand("add",new AddCommand());
        registerSubCommand("box",new BoxCommand());
        registerSubCommand("change",new ChangeCommand());
        registerSubCommand("delete",new DeleteCommand());
        registerSubCommand("get",new GetCommand());
        registerSubCommand("give",new GiveCommand());
        registerSubCommand("help",new HelpCommand());
        registerSubCommand("menu",new MenuCommand());
        registerSubCommand("papi",new PapiCommand());
        registerSubCommand("particle",new ParticleCommand());
        registerSubCommand("reload",new ReloadCommand());
        if(ConfigSetting.shop)
            registerSubCommand("shop",new ShopCommand());
    }

    public void registerSubCommand(String subCommand, CommandExecutor executor) {
        subCommands.put(subCommand.toLowerCase(), executor);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();
            CommandExecutor executor = subCommands.get(subCommand);
            if (executor != null) {
                return executor.onCommand(sender, command, label, args);
            }
        }else
            return subCommands.get("help").onCommand(sender, command, label, args);

        sender.sendMessage(ChatColor.RED+"使用/xgplottery help查看帮助");
        return true;
    }

}
