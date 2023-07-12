package cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl;

import cn.xgp.xgplottery.Gui.Impl.Anim.MarqueeAnimGui;
import cn.xgp.xgplottery.Listener.CloseListener;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Utils.MathUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class MarqueeAnimation extends LotteryAnimation {

    public MarqueeAnimation(Player player, Lottery lottery) {
        super(player, lottery);
    }


    @Override
    public String toLore() {
        return "跑马灯动画";
    }

    @Override
    public void playAnimation() {
        awards.add(getOneAward());
        MarqueeAnimGui gui = new MarqueeAnimGui();
        player.openInventory(gui.getInventory());
        int awardSlot = MathUtils.getRandomInt(0,20);
        IntStream.range(0,54).forEach(i->{
            gui.getInventory().setItem(i,lottery.showFakeItem());
        });
        gui.getInventory().setItem(awardSlot,awards.get(0).getRecordDisplayItem());
        int totalStep = 54+awardSlot;
        taskID = Bukkit.getScheduler().runTaskTimer(XgpLottery.instance, new Runnable() {
            int counter,step = 0;
            @Override
            public void run() {
                if(step==totalStep){
                    player.playSound(player.getLocation(), finish,1.0f,1.0f);
                    setStop(true);
                    cancelTask();
                }
                counter++;
                float pitch = (float) Math.pow(2.0, (float) step / totalStep);
                if (counter<54|| counter<162&&counter%2==0){
                    player.playSound(player.getLocation(), sound,0.2f,pitch);
                    gui.nexStep(step%54);
                    step++;
                }

            }
        }, 0L, 5L).getTaskId();
        CloseListener closeListener = new CloseListener(taskID,player.getUniqueId(),this,true);
        Bukkit.getPluginManager().registerEvents(closeListener,XgpLottery.instance);
    }
}
