package cn.xgp.xgplottery.Gui.Impl.Pool;

import cn.xgp.xgplottery.Gui.MyItem;
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

import java.text.DecimalFormat;

public class SpecialPoolGui extends LotteryGui{
    private final Inventory inv ;
    @Getter
    private final Lottery lottery;
    //TODO 文本自定义

    public SpecialPoolGui(Lottery lottery){
        this.lottery = lottery;
        inv = Bukkit.createInventory(this,6*9, ChatColor.YELLOW+ lottery.getName()+ "-保底物品内容");
    }
    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {

        if(lottery.getCalculator() instanceof Custom){
            int index =0;
            DecimalFormat df = new DecimalFormat("0.00%");
            for(int i =0;i<lottery.getSpItems().size();i++) {
                ItemStack item = lottery.getSpItems().get(i);
                MyItem guiItem = new MyItem(item);
                int weight = lottery.getSpWeights().get(i);
                int sum = lottery.getWeightSum();
                inv.setItem(index, guiItem
                        .setLore(ChatColor.GOLD +"权重/总权重："+ChatColor.GREEN+weight +"/"+sum)
                        .addLore(ChatColor.GOLD +"概率："+ChatColor.GREEN+df.format((double) weight/sum))
                        .getItem());
                index++;
            }
            for (index = 45;index<=53;index++){
                ItemStack borderGlass = new MyItem(Material.GRAY_STAINED_GLASS_PANE)
                        .setDisplayName(ChatColor.GRAY+"我也是有边界的>_<")
                        .setLore(ChatColor.GRAY+ "这是分界线捏，没有别的东西了~")
                        .getItem();
                inv.setItem(index,borderGlass);
            }

            inv.setItem(49,new MyItem(Material.ANVIL)
                    .setDisplayName(ChatColor.YELLOW+"操作指南")
                    .setLore(ChatColor.RED+"空手左键点击本物品返回列表")
                    .addLore(ChatColor.GOLD+"拖动物品点击加入物品")
                    .addLore(ChatColor.GOLD+"shift+右键点击删除物品")
                    .addLore(ChatColor.GOLD+"左键点击设置权重（越小概率越低）")
                    .getItem());

        }

        return this;
    }
}
