package cn.xgp.xgplottery.Lottery.LotteryAnimation;


import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import cn.xgp.xgplottery.Utils.NMSUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class LotteryAnimation {
    @Getter
    protected ProbabilityCalculator calculator;
    @Getter
    @Setter
    private boolean stop;
    protected Player player;
    @Getter
    protected Lottery lottery;
    @Getter
    protected static Sound sound;
    @Getter
    protected static Sound finish;

    protected List<Award> awards;
    protected int taskID;


    public LotteryAnimation(Player player, Lottery lottery){
        this.player = player;
        this.lottery = lottery;
        if(lottery!=null)
            this.calculator = lottery.getCalculatorObject();
        this.awards = new ArrayList<>();
    }
    public abstract String toLore();
    public abstract void playAnimation();
    public List<Award> getAwards(){
        return awards;
    }
    protected Award getOneAward(){
        return calculator.getAward(lottery,player);
    }
    protected void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
    static
    {
        if(NMSUtils.versionToInt<9){
            sound = Sound.valueOf("NOTE_PLING");
            finish = Sound.valueOf("LEVEL_UP");
        }
        else if(NMSUtils.versionToInt<13){
            sound = Sound.valueOf("BLOCK_NOTE_PLING");
            finish = Sound.valueOf("ENTITY_PLAYER_LEVELUP");
        }else {
            sound = Sound.BLOCK_NOTE_BLOCK_HARP;
            finish = Sound.valueOf("ENTITY_PLAYER_LEVELUP");
        }
    }
}
