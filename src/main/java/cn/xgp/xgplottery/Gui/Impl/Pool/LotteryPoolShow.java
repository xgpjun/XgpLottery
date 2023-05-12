package cn.xgp.xgplottery.Gui.Impl.Pool;

import cn.xgp.xgplottery.Gui.GuiItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl.Custom;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class LotteryPoolShow extends LotteryGui {
    private final Inventory inv ;
    @Getter
    private final Lottery lottery;
    //TODO 文本自定义
    public LotteryPoolShow(Lottery lottery){
        this.lottery = lottery;
        inv = Bukkit.createInventory(this,6*9, ChatColor.YELLOW+ lottery.getName()+ "-奖池内容");
    }
    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }
    @Override
    public LotteryGui loadGui() {
        int index =0;
        for(ItemStack item: lottery.getItems()) {
            GuiItem guiItem = new GuiItem(item);
            inv.setItem(index, guiItem.getItem());
            index++;
        }
        for (index = 45;index<=53;index++){
            ItemStack borderGlass = new GuiItem(Material.GRAY_STAINED_GLASS_PANE)
                    .setDisplayName(ChatColor.GRAY+"我也是有边界的>_<")
                    .setLore(ChatColor.GRAY+ "这是分界线捏，没有别的东西了~")
                    .getItem();
            inv.setItem(index,borderGlass);
        }
        inv.setItem(49,new GuiItem(Material.CHEST)
                .setDisplayName(ChatColor.YELLOW+"点击切换")
                .setLore(ChatColor.AQUA+"点击切换到保底物品")
                .getItem());
        return this;
    }

}
