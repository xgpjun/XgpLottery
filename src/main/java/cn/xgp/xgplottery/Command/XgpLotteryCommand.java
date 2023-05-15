package cn.xgp.xgplottery.Command;

import cn.xgp.xgplottery.Command.SubCmd.*;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class XgpLotteryCommand implements TabExecutor {
    private final Map<String, TabExecutor> subCommands = new HashMap<>();

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
        registerSubCommand("top",new TopCommand());
        registerSubCommand("shop",new ShopCommand());
    }

    public void registerSubCommand(String subCommand, TabExecutor executor) {
        subCommands.put(subCommand.toLowerCase(), executor);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();
            TabExecutor executor = subCommands.get(subCommand);
            if (executor != null) {
                return executor.onCommand(sender, command, label, args);
            }
        }else
            return subCommands.get("help").onCommand(sender, command, label, args);

        sender.sendMessage(ChatColor.RED+"使用/xgplottery help查看帮助");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>(subCommands.keySet());
        if(!sender.hasPermission("xgplottery.manager")){
            return Collections.singletonList("shop");
        }

        if(sender.hasPermission("xgplottery.manager")){
            if(args.length==1){
                return filter(list,args);
            }else {
                String subCommand = args[0].toLowerCase();
                TabExecutor executor = subCommands.get(subCommand);
                if (executor != null) {
                    return executor.onTabComplete(sender, command, label, args);
                }
            }
        }
        return null;

    }
    public static List<String> filter(List<String> list, String[] args) {
        String latest = null;
        if (args.length != 0) {
            latest = args[args.length - 1];
        }
        if (list.isEmpty() || latest == null)
            return list;
        String ll = latest.toLowerCase();
        list.removeIf(k -> !k.toLowerCase().startsWith(ll));
        return list;
    }
}
