package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Utils.NMSUtils;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Data
@Deprecated
public class LotteryNbtConverter{
    private String name;
    private String animation;
    private int maxTime;
    private List<String> itemsNBT;
    private List<Integer> weights;
    private List<String> spItemsNBT;
    private List<Integer> spWeights;
    private int value;
    private boolean isPoint;

    public LotteryNbtConverter(Lottery lottery) {
        this.name = lottery.getName();
        this.animation = lottery.getAnimation();
        this.maxTime = lottery.getMaxTime();
        this.weights = lottery.getWeights();

        this.spWeights = lottery.getSpWeights();
        this.value = lottery.getValue();
        this.isPoint = lottery.isPoint();

        this.itemsNBT = new ArrayList<>();
        this.spItemsNBT = new ArrayList<>();

        for (ItemStack itemStack : lottery.getItems()) {
            this.itemsNBT.add(NMSUtils.toNBTString(itemStack));
        }
        for (ItemStack itemStack : lottery.getSpItems()) {
            this.spItemsNBT.add(NMSUtils.toNBTString(itemStack));
        }
    }
    public Lottery toLottery(){
        List<ItemStack> items = new ArrayList<>();

        for(String nbtString:this.itemsNBT){
            ItemStack itemStack = NMSUtils.toItem(nbtString);
            if(itemStack.getItemMeta()==null)
                itemStack = new MyItem(Material.STONE).setDisplayName(ChatColor.RED+"Wrong Item").getItem();
            items.add(itemStack);
        }
        List<ItemStack> spItems = new ArrayList<>();
        for(String nbtString:this.spItemsNBT){
            ItemStack itemStack = NMSUtils.toItem(nbtString);
            if(itemStack.getItemMeta()==null)
                itemStack = new MyItem(Material.STONE).setDisplayName(ChatColor.RED+"Wrong Item").getItem();
            spItems.add(itemStack);
        }
        List<Award> awards = new ArrayList<>();
        for(int i = 0;i<items.size();i++){
            awards.add(new Award(items.get(i),weights.get(i)));
        }
        List<Award> spAwards = new ArrayList<>();
        for(int i = 0;i<spItems.size();i++){
            spAwards.add(new Award(spItems.get(i),spWeights.get(i)));
        }
        Lottery lottery = new Lottery(name,animation,maxTime,items,weights,spItems,spWeights,value,isPoint);
        lottery.setAwards(awards);
        lottery.setSpAwards(spAwards);
        return lottery;
    }
}