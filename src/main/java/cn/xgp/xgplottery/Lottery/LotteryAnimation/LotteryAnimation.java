package cn.xgp.xgplottery.Lottery.LotteryAnimation;


import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import cn.xgp.xgplottery.Utils.nmsUtils;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class LotteryAnimation {
    public abstract ProbabilityCalculator getCalculator();
    public abstract boolean isStop();
    public abstract String toLore();
    public abstract void playAnimation();
    public abstract ItemStack getAward();
    protected static Sound sound;
    @Getter
    protected static Sound finish;

    static
    {
        if(nmsUtils.versionToInt<9){
            sound = Sound.valueOf("NOTE_PLING");
            finish = Sound.valueOf("LEVEL_UP");
        }
        else if(nmsUtils.versionToInt<13){
            sound = Sound.valueOf("BLOCK_NOTE_PLING");
            finish = Sound.valueOf("ENTITY_PLAYER_LEVELUP");
        }else {
            sound = Sound.BLOCK_NOTE_BLOCK_HARP;
            finish = Sound.valueOf("ENTITY_PLAYER_LEVELUP");
        }
    }
}
