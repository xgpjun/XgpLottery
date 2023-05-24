package cn.xgp.xgplottery.Lottery.ProbabilityCalculator;

import cn.xgp.xgplottery.Lottery.Lottery;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ProbabilityCalculator {
    public static String[] calculatorList = {"Custom"};
    public abstract String getCalculatorType();
    public abstract String toLore();
    public abstract ItemStack getAward(Lottery lottery,Player player);
    public abstract void sendMessage();
}
