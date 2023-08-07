package cn.xgp.xgplottery.Gui.Impl.Reward;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryMenuGui;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.CumulativeRewards;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.ReceiveUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RewardGui extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9, LangUtils.Reward1);
    int page;
    int size;

    @Override
    public @NotNull Inventory getInventory() {
        return getPage(1);
    }

    @Override
    public LotteryGui loadGui() {
        setBorder(inv);
        inv.setItem(49,new MyItem(Material.ANVIL).setDisplayName(LangUtils.Reward13)
                .addLore(LangUtils.Reward14)
                .addLore(LangUtils.Reward15).getItem());
        inv.setItem(45,previousPage);
        inv.setItem(53,nextPage);
        return this;
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
                    .setDisplayName(LangUtils.Reward5 + rewards.getLotteryName())
                    .addLore(LangUtils.Reward16)
                            .addLore(LangUtils.Reward17)
                            .addLore(LangUtils.Reward9+str1)
                            .addLore(LangUtils.Reward10+str2)
                    .getItem());
            index++;
            if(index==4*7)
                break;
        }

        return inv;

    }

    public Inventory refresh(){
        return getPage(page);
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0: player.openInventory(new LotteryMenuGui().getInventory());break;
            //退出
            case 8: player.getOpenInventory().close();break;
            case 45:player.openInventory(getPage(this.page-1));break;
            case 53:player.openInventory(getPage(this.page+1));break;
            case 49:{
                ReceiveUtils.createRewardGift(player,this);
            }
            default:
        }
        ItemStack item = e.getCurrentItem();
        if(item !=null&&item.getType().equals(Material.CHEST)){
            int index = findSlot(e.getRawSlot());
            if(index==-1)
                return;
            CumulativeRewards rewards  = XgpLottery.rewards.get((this.page-1)*28+index);
            if(e.isShiftClick()&&e.isRightClick()){
                XgpLottery.rewards.remove((this.page-1)*28+index);
                player.openInventory(getPage(page));
            }else {
                player.openInventory(new RewardSetting(rewards,this).getInventory());
            }

        }
    }
}
