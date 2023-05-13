package cn.xgp.xgplottery.Command;

import cn.xgp.xgplottery.Command.SubCmd.HelpCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class XgpLotteryCommand implements CommandExecutor{
    private Map<String, CommandExecutor> subCommands = new HashMap<>();

    public XgpLotteryCommand(){
        registerSubCommand("help",new HelpCommand());
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

        // Handle the main command logic here
        // ...

        sender.sendMessage(ChatColor.RED+"使用/xgplottery help查看帮助");
        return true;
    }

}
