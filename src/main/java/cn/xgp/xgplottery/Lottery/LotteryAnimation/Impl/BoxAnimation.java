package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Gui.Impl.Anim.BoxAnimGui;
import cn.xgp.xgplottery.Listener.CloseListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import cn.xgp.xgplottery.Utils.VersionAdapterUtils;
import cn.xgp.xgplottery.XgpLottery;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoxAnimation extends LotteryAnimation {
    public boolean stop = false;
    private final Player player;
    private final Lottery lottery;
    private final boolean isCommand;
    @Getter
    ProbabilityCalculator calculator;
    @Getter
    ItemStack award;
    int taskID;

    public BoxAnimation(Player player,Lottery lottery,Boolean isCommand){
        this.player = player;
        this.lottery = lottery;
        this.isCommand = isCommand;

    }

    @Override
    public String toLore(){
        return "物品滚动动画";
    }

    @Override
    public boolean isStop(){
        return stop;
    }

    @Override
    public void playAnimation() {
        calculator = lottery.getCalculatorObject();
        award = calculator.getAward(lottery,player);

        Inventory inventory = new BoxAnimGui().loadGui().getInventory();
        List<ItemStack> showItemList = new ArrayList<>();
        initItemList(showItemList, lottery);
        //get award


        showItemList.set(20,award);
        player.openInventory(inventory);
        if(!isCommand){
            ItemStack item = VersionAdapterUtils.getItemInMainHand(player);

            if (item.getAmount() <= 1) {
                VersionAdapterUtils.setItemInMainHand(player,null);
            } else {
                item.setAmount(item.getAmount()-1);
            }
        }

        taskID = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance, new Runnable() {
            int j = 0;
            @Override
            public void run() {

                for (int i = 9; i < 18; i++) {
                    MyItem myItem = new MyItem(showItemList.get(i-9+j));
                    if(i==13)
                        inventory.setItem(i,myItem.addEnchant().getItem());
                    else
                        inventory.setItem(i,myItem.getItem());
                }
                j++;
                if (j >= showItemList.size() - 9) {
                    j = 0;
                }
                if (j == 17) { // 中奖物品的位置是第 20 个 21-4
                    stop = true;
                }
                float pitch = (float) Math.pow(2.0, j / 18.0);
                player.playSound(player.getLocation(), sound,1.0f,pitch);
                if (stop) {
                    cancelTask();
                    //给与物品
                    player.playSound(player.getLocation(), finish,1.0f,1.0f);
                    player.getInventory().addItem(award);
                    calculator.sendMessage();
                }

            }


        }, 0L, 5L).getTaskId();
        CloseListener closeListener = new CloseListener(taskID,player.getUniqueId(),this);
        Bukkit.getPluginManager().registerEvents(closeListener,XgpLottery.instance);
    }

    private void initItemList(List<ItemStack> showItemList,Lottery lottery){
        Random random = new Random();
        int item = lottery.getAmount();
        int sp = lottery.getSpAmount();
        for (int i = 0; i <= 40; i++) {
            int next = random.nextInt(item+sp);
            ItemStack itemStack = next>=item?lottery.getSpItems().get(next-item).clone():lottery.getItems().get(next).clone();
            showItemList.add(itemStack);
        }
    }
    private void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

}
