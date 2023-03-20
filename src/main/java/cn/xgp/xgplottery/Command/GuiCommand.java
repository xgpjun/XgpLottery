package cn.xgp.xgplottery.Command;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.Impl.LotteryMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GuiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player= (Player) sender;
        LotteryGui Gui;
        if(args.length==1&&args[0].equals("open")){
            Gui = new LotteryMenu("XgpLottery菜单").LoadGui();
            player.openInventory(Gui.getInventory());
        }
        //TODO 文本自定义
        return true;
    }
}
