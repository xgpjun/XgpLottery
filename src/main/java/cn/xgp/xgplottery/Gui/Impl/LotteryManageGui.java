package cn.xgp.xgplottery.Gui.Impl;

import cn.xgp.xgplottery.Gui.GuiItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
//管理奖池GUI
public class LotteryManageGui extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9,"奖池管理");
    public LotteryManageGui(){
    }

    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        //最大4*7
        int index = 0;
        for(Lottery lottery:XgpLottery.lotteryList.values()){
            int maxTime = lottery.getMaxTime();
            String mt = maxTime>0? String.valueOf(maxTime) :"未设置保底次数";
            inv.setItem(slot[index],new GuiItem(Material.CHEST)
                    .setDisplayName(ChatColor.BLUE+"奖池 :"+ChatColor.AQUA + lottery.getName())
                    .setLore(ChatColor.GOLD+"抽奖动画："+ChatColor.RESET+""+ChatColor.GREEN +lottery.getAnimationType(),
                            ChatColor.GOLD+"保底次数："+ChatColor.RESET+""+ChatColor.GREEN +mt,
                            ChatColor.AQUA +"shift+左键点击设置保底",
                            ChatColor.AQUA +"左键打开奖池，右键打开保底池")
                    .getItem());

            index++;
            if(index==4*7)
                break;
        }
        setBorder(inv);
        return this;
    }
}
