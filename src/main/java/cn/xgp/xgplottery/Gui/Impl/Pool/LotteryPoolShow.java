package cn.xgp.xgplottery.Gui.Impl.Pool;

import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Objects;


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

        if(ConfigSetting.showProbability){
            DecimalFormat df = new DecimalFormat("0.00%");
            for(int i =0;i<lottery.getItems().size();i++) {
                ItemStack item = lottery.getItems().get(i);
                MyItem guiItem = new MyItem(item);
                int weight = lottery.getWeights().get(i);
                int sum = lottery.getWeightSum();
                guiItem.setLore(ChatColor.GOLD +"概率："+ChatColor.GREEN+df.format((double) weight/sum));
                inv.setItem(i, guiItem.getItem());
            }
        }else {
            for(int i =0;i<lottery.getItems().size();i++) {
                MyItem guiItem = new MyItem(lottery.getItems().get(i));
                inv.setItem(i, guiItem.getItem());
            }
        }

        for (int index = 45;index<=53;index++){
            inv.setItem(index,borderGlass);
        }
        inv.setItem(49,new MyItem(Material.CHEST)
                .setDisplayName(ChatColor.YELLOW+"点击切换")
                .setLore(ChatColor.AQUA+"点击切换到保底物品")
                .getItem());
        return this;
    }
    public void handleClick(InventoryClickEvent e){
        if(e.isLeftClick()&&e.getRawSlot()==49){
            Lottery lottery = ((LotteryPoolShow) Objects.requireNonNull(e.getInventory().getHolder())).getLottery();
            e.getWhoClicked().openInventory(new SpecialPoolShow(lottery).getInventory());
        }
    }

}
