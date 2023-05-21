package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.Utils.nmsUtils;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Data
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
            this.itemsNBT.add(nmsUtils.toNBTString(itemStack));
        }
        for (ItemStack itemStack : lottery.getSpItems()) {
            this.spItemsNBT.add(nmsUtils.toNBTString(itemStack));
        }
    }
    public Lottery toLottery(){
        List<ItemStack> items = new ArrayList<>();
        for(String nbtString:this.itemsNBT){
            items.add(nmsUtils.toItem(nbtString));
        }
        List<ItemStack> spItems = new ArrayList<>();
        for(String nbtString:this.spItemsNBT){
            spItems.add(nmsUtils.toItem(nbtString));
        }
        return new Lottery(name,animation,maxTime,items,weights,spItems,spWeights,value,isPoint);
    }
}