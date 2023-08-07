package cn.xgp.xgplottery.Gui.Impl.Reward;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RewardShow extends PlayerGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9, LangUtils.Reward);
    private final List<Award> awards;
    private final PlayerRewardGui returnGui;

    public RewardShow(List<Award> awards, PlayerRewardGui returnGui){
        this.awards = awards;
        this.returnGui = returnGui;
    }
    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        setBorder(inv);
        inv.setItem(49,new MyItem(Material.DIAMOND).setDisplayName(LangUtils.AnvilText1)
                .addLore(LangUtils.AnvilText2)
                .getItem());


        for (int i = 0; i< awards.size(); i++){
            Award award = awards.get(i);
            inv.setItem(slot[i],new MyItem(award.getRecordDisplayItem()).getItem());
            if(i==4*7)
                break;
        }
        return this;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0: player.openInventory(returnGui.refresh());break;
            //退出
            case 8: player.getOpenInventory().close();break;
            case 49: player.openInventory(returnGui.refresh());break;
            default:
        }
    }
}
