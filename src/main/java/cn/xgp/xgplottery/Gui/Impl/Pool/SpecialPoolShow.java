package cn.xgp.xgplottery.Gui.Impl.Pool;

import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.Utils.LangUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Objects;

public class SpecialPoolShow extends PlayerGui {
    private final Inventory inv ;
    @Getter
    private final Lottery lottery;

    public SpecialPoolShow(Lottery lottery){
        this.lottery = lottery;
        inv = Bukkit.createInventory(this,6*9, ChatColor.YELLOW+ lottery.getName()+ "-"+ LangUtils.GuaranteedAwardList);
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
            for(int i =0;i<lottery.getSpItems().size();i++) {
                ItemStack item = lottery.getSpItems().get(i);
                MyItem guiItem = new MyItem(item);
                int weight = lottery.getSpWeights().get(i);
                int sum = lottery.getWeightSum();
                guiItem.setLore(ChatColor.GOLD + LangUtils.Probability +ChatColor.GREEN+df.format((double) weight/sum))
                        .addLore(ChatColor.GOLD+ LangUtils.ProbabilityInGuaranteedAward +ChatColor.GREEN+df.format((double) weight/ lottery.getSpWeightSum()));
                inv.setItem(i, guiItem.getItem());
            }
        }else {
            for(int i =0;i<lottery.getSpItems().size();i++) {
                MyItem guiItem = new MyItem(lottery.getSpItems().get(i));
                inv.setItem(i, guiItem.getItem());
            }
        }

        for (int index = 45;index<=53;index++){
            inv.setItem(index,borderGlass);
        }
        inv.setItem(49,new MyItem(Material.CHEST)
                .setDisplayName(ChatColor.YELLOW+LangUtils.Switch)
                .setLore(ChatColor.AQUA+LangUtils.SwitchToNormal)
                .getItem());
        return this;
    }
    public void handleClick(InventoryClickEvent e){
        if(e.isLeftClick()&&e.getRawSlot()==49){
            Lottery lottery = ((SpecialPoolShow) Objects.requireNonNull(e.getInventory().getHolder())).getLottery();
            e.getWhoClicked().openInventory(new LotteryPoolShow(lottery).getInventory());
        }
    }

}
