package cn.xgp.xgplottery.Gui.Impl.Manage.LotterySettings;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PoolGui;
import cn.xgp.xgplottery.Lottery.Award;
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

public class AwardSetting extends LotteryGui {
    Award award;
    Inventory inv = Bukkit.createInventory(this,6*9, ChatColor.GOLD+ "奖品设置！");
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
        inv.setItem(19,new MyItem(item).setDisplayName(ChatColor.GOLD+"上方是具体发放的物品")
                .addLore(ChatColor.BLUE+"设置物品: "+ChatColor.AQUA+"拖动物品在此处点击")
                .setAmount(1)
                .getItem());
        //是否给与物品
        String isGiveItem = award.isGiveItem()?"是":"否";
        MyItem guiItem = new MyItem(item).setDisplayName(ChatColor.GOLD+"是否给予物品")
                .setLore(ChatColor.BLUE+"当前状态："+ChatColor.AQUA+isGiveItem)
                .addLore(ChatColor.BLUE+"切换: "+ChatColor.AQUA+"左键")
                .setAmount(1);
        if(award.isGiveItem()){
            guiItem.addEnchant();
        }
        inv.setItem(28,guiItem.getItem());

        //指令
        inv.setItem(12,new MyItem(Material.PAPER).setDisplayName(ChatColor.GOLD+"奖池指令")
                .setLore(ChatColor.BLUE+"依次执行的指令：")
                .addLore(award.getCommandsString())
                .getItem());
        //是否执行指令
        String isExecuteCommands = award.isExecuteCommands()?"是":"否";
        guiItem = new MyItem(command).setDisplayName(ChatColor.GOLD+"是否执行指令")
                .setLore(ChatColor.BLUE+"当前状态："+ChatColor.AQUA+isExecuteCommands)
                .addLore(ChatColor.BLUE+"切换状态: "+ChatColor.AQUA+"点击本物品切换状态");
        if(award.isExecuteCommands()){
            guiItem.addEnchant();
        }
        inv.setItem(21,guiItem.getItem());
        //添加指令
        inv.setItem(30,new MyItem(command).setDisplayName(ChatColor.GOLD+"添加奖池指令")
                .addLore(ChatColor.BLUE+"添加: "+ChatColor.AQUA+"左键点击")
                .addLore(ChatColor.BLUE+"支持占位符如: "+ChatColor.AQUA+"%player_name%")
                .addLore(ChatColor.GREEN+"添加一个命令，以控制台执行")
                .getItem());
        //删除指令
        inv.setItem(39,new MyItem(command).setDisplayName(ChatColor.GOLD+"删除奖池指令")
                .addLore(ChatColor.BLUE+"删除一条指令: "+ChatColor.AQUA+"左键点击")
                .getItem());
        //是否播报
        String isBroadCast = award.isBroadCast()?"是":"否";
        guiItem = new MyItem(Material.NOTE_BLOCK).setDisplayName(ChatColor.GOLD+"是否播报")
                .setLore(ChatColor.BLUE+"当前状态："+ChatColor.AQUA+isBroadCast)
                .addLore(ChatColor.BLUE+"切换: "+ChatColor.AQUA+"左键点击");
        if(award.isBroadCast()){
            guiItem.addEnchant();
        }
        inv.setItem(14, guiItem.getItem());

        //物品显示名称（在奖池预览）
        String displayName = award.getDisplayName() != null ? award.getDisplayName() : ChatColor.GOLD + "暂无展示名";
        inv.setItem(23, new MyItem(writable_book)
                .setDisplayName(displayName)
                .addLore(ChatColor.BLUE + "设置: " + ChatColor.AQUA + "左键点击")
                .addLore(ChatColor.BLUE + "用于奖池预览与播报模块")
                .addLore(ChatColor.BLUE + "本物品的名称即为展示名，支持颜色符号 &")
                .getItem());
        guiItem = new MyItem(writable_book)
                .setDisplayName(ChatColor.GOLD + "强制显示原物品")
                .addLore(ChatColor.BLUE + "当前状态:" + ChatColor.AQUA + (award.isShowRaw() ? "是" : "否"))
                .addLore(ChatColor.GREEN + "如果物品在展示中出现了错误，请打开此项");
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
