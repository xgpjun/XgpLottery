package cn.xgp.xgplottery.Gui.Impl.Manage.LotterySettings;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PoolGui;
import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
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

public class AwardSetting extends LotteryGui {
    Award award;
    Inventory inv = Bukkit.createInventory(this,6*9, LangUtils.AwardSetting);
    PoolGui returnGui;

    public AwardSetting(List<Award> awards,int index,PoolGui returnGui){
        this.award = awards.get(index);
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

        ItemStack item = award.getItem();
        //物品
        inv.setItem(10,new MyItem(item).getItem());
        //设置物品
        inv.setItem(19,new MyItem(item).setDisplayName(LangUtils.AwardItem1)
                .addLore(LangUtils.AwardItem2)
                .setAmount(1)
                .getItem());
        //是否给与物品
        String isGiveItem = award.isGiveItem()?LangUtils.True:LangUtils.False;
        MyItem guiItem = new MyItem(item).setDisplayName(LangUtils.AwardItem3)
                .setLore(LangUtils.AwardItem4+ChatColor.AQUA+isGiveItem)
                .addLore(LangUtils.Operation+LangUtils.LeftClick)
                .setAmount(1);
        if(award.isGiveItem()){
            guiItem.addEnchant();
        }
        inv.setItem(28,guiItem.getItem());

        //指令
        inv.setItem(12,new MyItem(Material.PAPER).setDisplayName(LangUtils.AwardCmd1)
                .setLore(LangUtils.AwardCmd2)
                .addLore(award.getCommandsString())
                .getItem());
        //是否执行指令
        String isExecuteCommands = award.isExecuteCommands()?LangUtils.True:LangUtils.False;
        guiItem = new MyItem(command).setDisplayName(LangUtils.AwardCmd3)
                .setLore(LangUtils.AwardCmd4+ChatColor.AQUA+isExecuteCommands)
                .addLore(LangUtils.Operation+LangUtils.LeftClick);
        if(award.isExecuteCommands()){
            guiItem.addEnchant();
        }
        inv.setItem(21,guiItem.getItem());
        //添加指令
        inv.setItem(30,new MyItem(command).setDisplayName(LangUtils.AwardCmd5)
                .addLore(LangUtils.Operation+LangUtils.LeftClick)
                .addLore(LangUtils.AwardCmd6)
                .addLore(LangUtils.AwardCmd7)
                .getItem());
        //删除指令
        inv.setItem(39,new MyItem(command).setDisplayName(LangUtils.AwardCmd8)
                .addLore(LangUtils.Operation+LangUtils.LeftClick)
                .getItem());
        //是否播报
        String isBroadCast = award.isBroadCast()?LangUtils.True:LangUtils.False;
        guiItem = new MyItem(Material.NOTE_BLOCK).setDisplayName(LangUtils.AwardBroadCast1)
                .setLore(LangUtils.AwardCmd4+ChatColor.AQUA+isBroadCast)
                .addLore(LangUtils.Operation+LangUtils.LeftClick);
        if(award.isBroadCast()){
            guiItem.addEnchant();
        }
        inv.setItem(14, guiItem.getItem());

        //物品显示名称（在奖池预览）
        String displayName = award.getDisplayName() != null ? award.getDisplayName() : LangUtils.AwardDisplayName1;
        inv.setItem(23, new MyItem(writable_book)
                .setDisplayName(displayName)
                .addLore(LangUtils.Operation+LangUtils.LeftClick)
                .addLore(LangUtils.AwardDisplayName2)
                .addLore(LangUtils.AwardDisplayName3)
                .getItem());
        guiItem = new MyItem(writable_book)
                .setDisplayName(LangUtils.AwardShowRawItem1)
                .addLore(LangUtils.AwardCmd4 + ChatColor.AQUA + (award.isShowRaw() ? LangUtils.True:LangUtils.False))
                .addLore(LangUtils.AwardShowRawItem2);
        if (award.isShowRaw()) {
            guiItem.addEnchant();
        }
        inv.setItem(32, guiItem.getItem());

        return this;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();
        switch (slot){
            //设置奖品
            case 19:{
                ItemStack cursorItem = e.getCursor();
                if(cursorItem==null){ return; }
                ItemStack item = cursorItem.clone();
                // 把玩家拿起的物品放入背包
                player.setItemOnCursor(null);
                player.getInventory().addItem(cursorItem);
                award.setItem(item);
                player.openInventory(this.getInventory());
                SerializeUtils.saveLotteryData();
                return;

            }
            //是否给予物品
            case 28:{
                award.setGiveItem(!award.isGiveItem());
                player.openInventory(this.getInventory());
                SerializeUtils.saveLotteryData();
                return;
            }
            //是否执行指令
            case 21:{
                award.setExecuteCommands(!award.isExecuteCommands());
                player.openInventory(this.getInventory());
                SerializeUtils.saveLotteryData();
                return;
            }
            //添加指令
            case 30:{
                ReceiveUtils.addCommand(player,this,award);
                SerializeUtils.saveLotteryData();
                return;
            }
            //删除指令
            case 39:{
                if(award.getCommands().size()==0){
                    return;
                }
                ReceiveUtils.delCommand(player,this,award);
                SerializeUtils.saveLotteryData();
                return;
            }
            //是否播报
            case 14:{
                award.setBroadCast(!award.isBroadCast());
                player.openInventory(this.getInventory());
                SerializeUtils.saveLotteryData();
                return;
            }
            //设置显示名称
            case 23: {
                ReceiveUtils.setAwardDisplayName(player, this, award);
                SerializeUtils.saveLotteryData();
                return;
            }
            case 32: {
                award.setShowRaw(!award.isShowRaw());
                player.openInventory(this.getInventory());
                SerializeUtils.saveLotteryData();
                return;
            }

            //上一层
            case 0:
                player.openInventory(returnGui.refresh());
                break;
            //退出
            case 8:
                player.getOpenInventory().close();
                return;

            default:
        }
    }
}
