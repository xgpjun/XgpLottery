package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Gui.Impl.Anim.SelectItemGui;
import cn.xgp.xgplottery.Listener.CloseListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import cn.xgp.xgplottery.Utils.VersionAdapterUtils;
import cn.xgp.xgplottery.XgpLottery;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class SelectItemAnimation extends LotteryAnimation {

    public boolean stop = false;
    private final Player player;
    @Getter
    private final Lottery lottery;
    private final boolean isCommand;

    @Getter
    private ItemStack award ;
    @Getter
    ProbabilityCalculator calculator;


    public SelectItemAnimation(Player player, Lottery lottery, boolean isCommand) {
        this.player = player;
        this.lottery = lottery;
        this.isCommand = isCommand;

    }

    @Override
    public String toLore() {
        return "物品选择动画";
    }
    @Override
    public boolean isStop(){
        return stop;
    }

    @Override
    public void playAnimation() {
        calculator = lottery.getCalculatorObject();
        award = calculator.getAward(lottery,player);

        Inventory inventory = new SelectItemGui(this).loadGui().getInventory();
        if(!isCommand){
            ItemStack item = VersionAdapterUtils.getItemInMainHand(player);

            if (item.getAmount() <= 1) {
                VersionAdapterUtils.setItemInMainHand(player,null);
            } else {
                item.setAmount(item.getAmount()-1);
            }
        }
        player.openInventory(inventory);

        int taskID = Bukkit.getScheduler().runTaskLater(XgpLottery.instance, () -> {
            if(!stop) {
                player.playSound(player.getLocation(), finish, 1.0f, 1.0f);
                inventory.setItem(new Random().nextInt(54), new MyItem(award).addEnchant().getItem());
                calculator.sendMessage();
            }
            stop = true;
        }, 200).getTaskId();
        CloseListener closeListener = new CloseListener(taskID,player.getUniqueId(),this);
        Bukkit.getPluginManager().registerEvents(closeListener,XgpLottery.instance);
    }
}
