package cn.xgp.xgplottery.Gui.Impl.Reward;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Lottery.CumulativeRewards;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.TimesUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerRewardGui extends PlayerGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9, LangUtils.Reward);
    Player player;
    int page;
    int size;
    public PlayerRewardGui(Player player){
        this.player = player;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return getPage(1);
    }

    @Override
    public LotteryGui loadGui() {
        setBorder(inv);
        inv.setItem(45,previousPage);
        inv.setItem(53,nextPage);
        return this;
    }
    public Inventory refresh(){
        return getPage(page);
    }

    Inventory getPage(int page){
        inv.clear();
        loadGui();
        size =(int) Math.ceil( (double) XgpLottery.lotteryList.size() / 28);
        this.page = Math.max(1, Math.min(page, size));
        int index = 0;
        for (int i = (this.page - 1) * 28; i<XgpLottery.rewards.size(); i++){
            CumulativeRewards rewards = XgpLottery.rewards.get(i);

            String str1 = rewards.getNeededTimes()==0?LangUtils.Reward3: String.valueOf(rewards.getNeededTimes());
            String str2 = rewards.getLimit()==0?LangUtils.Reward4: String.valueOf(rewards.getLimit());

            inv.setItem(slot[index],new MyItem(Material.CHEST)
                    .setDisplayName(LangUtils.Reward5+ rewards.getLotteryName())
                    .addLore(LangUtils.Reward6)
                    .addLore(LangUtils.Reward7)
                    .addLore(LangUtils.Reward8+ TimesUtils.getTimes(player.getUniqueId(), rewards.getLotteryName()))
                    .addLore(LangUtils.Reward9+str1)
                    .addLore(LangUtils.Reward10+str2)
                    .addLore(LangUtils.Reward11+rewards.canGetTimes(player))
                    .addLore(LangUtils.Reward12+rewards.hasGetTimes(player))
                    .getItem());
            index++;
            if(index==4*7)
                break;
        }

        return inv;

    }


    @Override
    public void handleClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            case 0:
                //退出
            case 8:
                player.getOpenInventory().close();break;
            case 45:player.openInventory(getPage(this.page-1));break;
            case 53:player.openInventory(getPage(this.page+1));break;
            default:
        }
        ItemStack item = e.getCurrentItem();
        if(item !=null&&item.getType().equals(Material.CHEST)){
            int index = findSlot(e.getRawSlot());
            if(index==-1)
                return;
            CumulativeRewards rewards  = XgpLottery.rewards.get((this.page-1)*28+index);
            if(e.isRightClick()){
                rewards.giveRewards(player);
                player.openInventory(getPage(page));
            }else {
                player.openInventory(new RewardShow(rewards.getAwards(),this).getInventory());
            }

        }
    }
}
