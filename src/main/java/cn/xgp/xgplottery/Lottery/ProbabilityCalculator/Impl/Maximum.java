package cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl;

import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Maximum extends ProbabilityCalculator {
    @Override
    public String getCalculatorType() {
        return "Maximum";
    }
    @Override
    public String toLore() {
        return "保底概率";
    }

    @Override
    public void giveItem(Lottery lottery, Player player) {

    }

    @Override
    public ItemStack getAward(Lottery lottery,Player player) {
        return null;
    }
}
