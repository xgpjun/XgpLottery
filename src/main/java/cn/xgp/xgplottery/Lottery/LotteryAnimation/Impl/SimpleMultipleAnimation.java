package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Gui.Impl.Anim.SimpleMultipleAnimGui;
import cn.xgp.xgplottery.Listener.CloseListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.MultipleAnimation;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class SimpleMultipleAnimation extends MultipleAnimation {

    public SimpleMultipleAnimation(Player player, Lottery lottery) {
        super(player, lottery);
    }

    @Override
    public String toLore() {
        return "简易十连抽动画";
    }

    @Override
    public void playAnimation() {
        IntStream.range(0, 10).forEach(i -> awards.add(getOneAward()));
        SimpleMultipleAnimGui gui = new SimpleMultipleAnimGui();

        player.openInventory(gui.getInventory());
        taskID = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance, new Runnable() {
            int counter = 0;
            @Override
            public void run() {
                gui.setNextItem(awards.get(counter).getRecordDisplayItem());
                float pitch = (float) Math.pow(2.0, counter / 10.0);
                player.playSound(player.getLocation(), sound,0.2f,pitch);
                counter++;
                if(counter==10){
                    player.playSound(player.getLocation(), finish,1.0f,1.0f);
                    setStop(true);
                    cancelTask();
                }

            }
        }, 30L, 20L).getTaskId();
        CloseListener closeListener = new CloseListener(taskID,player.getUniqueId(),this,true);
        Bukkit.getPluginManager().registerEvents(closeListener,XgpLottery.instance);
    }
}
