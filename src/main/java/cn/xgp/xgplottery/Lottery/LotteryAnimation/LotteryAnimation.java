package cn.xgp.xgplottery.Lottery.LotteryAnimation;


import cn.xgp.xgplottery.Lottery.Lottery;
import org.bukkit.entity.Player;

public abstract class LotteryAnimation {
    public static String[] animationList = {"BoxAnimation"};
    public abstract String getAnimationType();
    public abstract String toLore();
    public abstract void playAnimation();
}
