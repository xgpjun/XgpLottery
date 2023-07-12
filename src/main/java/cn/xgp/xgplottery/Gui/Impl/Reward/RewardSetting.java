package cn.xgp.xgplottery.Gui.Impl.Reward;


import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.CumulativeRewards;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.ReceiveUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RewardSetting extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9, ChatColor.GOLD+ "累抽奖励设置!");
    private final List<Award> awards;
    private final CumulativeRewards rewards;
    private final RewardGui returnGui;

    public RewardSetting(CumulativeRewards rewards,RewardGui returnGui){
        this.rewards = rewards;
        this.awards = rewards.getAwards();
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
        inv.setItem(49,new MyItem(Material.ANVIL).setDisplayName(ChatColor.GOLD+"操作指南！")
                        .addLore(ChatColor.BLUE+"点击本物品返回")
                        .addLore(ChatColor.BLUE+"添加奖品: "+ChatColor.AQUA+"将一个物品放在此处！")
                .getItem());

        inv.setItem(47,new MyItem(Material.ANVIL).setDisplayName(ChatColor.GOLD+"需要的抽奖次数！")
                .addLore(ChatColor.BLUE+"更改:"+ChatColor.AQUA+"左键")
                        .addLore(ChatColor.GREEN+"每到达一次这个数值皆可领一次,0为取消领取")
                .getItem());

        inv.setItem(51,new MyItem(Material.ANVIL).setDisplayName(ChatColor.GOLD+"可领取的次数")
                .addLore(ChatColor.BLUE+"更改:"+ChatColor.AQUA+"左键")
                .addLore(ChatColor.GREEN+"0为无限领取次数")
                .getItem());

        for (int i = 0; i< awards.size(); i++){
            Award award = awards.get(i);
            inv.setItem(slot[i],new MyItem(award.getRecordDisplayItem())
                    .addLore(ChatColor.BLUE+ "设置奖品具体信息:"+ChatColor.AQUA+"左键点击!")
                            .addLore(ChatColor.BLUE+ "删除奖项"+ChatColor.RED+"Shift+右键点击!")
                    .getItem());
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
            case 49:{
                if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                    // 玩家拿起了物品，执行相关操作
                    ItemStack cursorItem = e.getCursor();
                    ItemStack item = cursorItem.clone();
                    // 把玩家拿起的物品放入背包
                    player.setItemOnCursor(null);
                    player.getInventory().addItem(cursorItem);

                    Award award = new Award(item);
                    awards.add(award);
                    player.openInventory(getInventory());
                    SerializeUtils.saveRewardData();
                }
                else {
                    // 玩家没有拿起物品，处理点击铁砧返回
                    player.openInventory(returnGui.refresh());
                }
                break;
            }
            case 47: ReceiveUtils.setTimes(player,this,rewards::setNeededTimes); break;
            case 51: ReceiveUtils.setTimes(player,this,rewards::setLimit); break;
            default:
        }

        int index = findSlot(e.getRawSlot());
        if(index==-1)
            return;
        //删除
        if(e.isShiftClick()&&e.isRightClick()&&e.getCurrentItem()!=null){
            awards.remove(index);
            player.openInventory(getInventory());
            SerializeUtils.saveRewardData();
        }
        //设置
        if(!e.isShiftClick()&&e.isLeftClick()&&e.getCurrentItem()!=null){
            player.openInventory(new AwardInRewardSetting(awards.get(index),this).getInventory());
        }
    }
}
