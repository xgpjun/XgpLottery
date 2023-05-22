package cn.xgp.xgplottery.Gui.Impl.Manage;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
//创建奖池GUI
public class LotteryCreateGui extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9,ChatColor.GOLD+"创建奖池");


    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        setBorder(inv);
        //创建奖池
        MyItem manageIcon = new MyItem(Material.ANVIL);
        inv.setItem(22,manageIcon
                .setDisplayName(ChatColor.BLUE+"点击此处创建一个抽奖池")
                .setLore(ChatColor.GREEN+"创建一个奖池")
                .getItem());
        return this;
    }
    public void handleClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0: player.openInventory(new LotteryMenuGui().getInventory());break;
            //退出
            case 8: player.getOpenInventory().close();break;
            case 22: Lottery.createLottery(player); break;
            default:
        }
    }
}
