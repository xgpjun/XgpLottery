package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Gui.Impl.Anim.SelectItemGui;
import cn.xgp.xgplottery.Listener.CloseListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.GiveUtils;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SelectItemAnimation extends LotteryAnimation {

    public SelectItemAnimation(Player player, Lottery lottery) {
        super(player,lottery);
    }

    @Override
    public String toLore() {
        return LangUtils.SelectItemAnimation;
    }

    @Override
    public void playAnimation() {
        awards.add(getOneAward());

        Inventory inventory = new SelectItemGui(this).loadGui().getInventory();
        player.openInventory(inventory);
        taskID = Bukkit.getAsyncScheduler().runDelayed(XgpLottery.instance, scheduledTask -> {
            if(!isStop()) {
                player.playSound(player.getLocation(), finish, 1.0f, 1.0f);
                inventory.setItem(new Random().nextInt(54), new MyItem(awards.get(0).getRecordDisplayItem()).addEnchant().getItem());
                GiveUtils.giveAward(player,awards);
                calculator.sendMessage();
            }
            setStop(true);
        },10, TimeUnit.SECONDS);

        CloseListener closeListener = new CloseListener(taskID,player.getUniqueId(),this);
        Bukkit.getPluginManager().registerEvents(closeListener,XgpLottery.instance);
    }
}
