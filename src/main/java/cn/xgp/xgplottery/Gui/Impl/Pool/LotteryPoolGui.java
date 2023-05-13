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

//奖池详细内容
public class LotteryPoolGui extends LotteryGui {
    private final Inventory inv ;
    @Getter
    private final Lottery lottery;
    //TODO 文本自定义


    public LotteryPoolGui(Lottery lottery){
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

        if(lottery.getCalculator() instanceof Custom){
            DecimalFormat df = new DecimalFormat("0.00%");
            int index =0;
            for(int i =0;i<lottery.getItems().size();i++) {
                ItemStack item = lottery.getItems().get(i);
                MyItem guiItem = new MyItem(item);
                int weight = lottery.getWeights().get(i);
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
