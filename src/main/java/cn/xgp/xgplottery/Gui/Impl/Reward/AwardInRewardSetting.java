package cn.xgp.xgplottery.Gui.Impl.Reward;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.ReceiveUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class AwardInRewardSetting extends LotteryGui {
    Award award;
    final Inventory inv = Bukkit.createInventory(this,6*9, ChatColor.GOLD+ "奖品设置！");
    LotteryGui returnGui;
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
        inv.setItem(19,new MyItem(item).setDisplayName(ChatColor.GOLD+"设置物品")
                .setLore(ChatColor.BLUE+"设置物品："+ChatColor.AQUA+"拖动物品在此处点击")
                .addLore(ChatColor.BLUE+"上方的物品是具体发放的物品")
                .setAmount(1)
                .getItem());
        //是否给与物品
        String isGiveItem = award.isGiveItem()?"是":"否";
        inv.setItem(28,new MyItem(item).setDisplayName(ChatColor.GOLD+"是否给予物品")
                .setLore(ChatColor.BLUE+"当前状态："+ChatColor.AQUA+isGiveItem)
                .addLore(ChatColor.BLUE+"点击本物品切换状态")
                .setAmount(1)
                .getItem());

        //指令
        inv.setItem(12,new MyItem(Material.PAPER).setDisplayName(ChatColor.GOLD+"奖池指令")
                .setLore(ChatColor.BLUE+"依次执行的指令：")
                .addLore(award.getCommandsString())
                .getItem());
        //是否执行指令
        String isExecuteCommands = award.isExecuteCommands()?"是":"否";
        inv.setItem(21,new MyItem(command).setDisplayName(ChatColor.GOLD+"是否执行指令")
                .setLore(ChatColor.BLUE+"当前状态："+ChatColor.AQUA+isExecuteCommands)
                .addLore(ChatColor.BLUE+"点击本物品切换状态")
                .getItem());
        //添加指令
        inv.setItem(30,new MyItem(command).setDisplayName(ChatColor.GOLD+"添加奖池指令")
                .setLore(ChatColor.BLUE+"添加一个命令，以控制台执行")
                .addLore(ChatColor.BLUE+"支持占位符: 如%Player%")
                .getItem());
        //删除指令
        inv.setItem(39,new MyItem(command).setDisplayName(ChatColor.GOLD+"删除奖池指令")
                .setLore(ChatColor.BLUE+"点击删除指令")
                .getItem());
        //物品显示名称（在奖池预览）
        String displayName = award.getDisplayName()!=null?award.getDisplayName():ChatColor.GOLD+"暂无展示名";
        inv.setItem(23,new MyItem(Material.WRITABLE_BOOK)
                .setDisplayName(displayName)
                .setLore(ChatColor.BLUE+"点击本物品设置展示名")
                .addLore(ChatColor.BLUE+"用于奖池预览与播报模块")
                .addLore(ChatColor.BLUE+"本物品的名称即为展示名，支持颜色符号")
                .getItem());
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
                SerializeUtils.saveRewardData();
                return;

            }
            //是否给予物品
            case 28:{
                award.setGiveItem(!award.isGiveItem());
                player.openInventory(this.getInventory());
                SerializeUtils.saveRewardData();
                return;
            }
            //是否执行指令
            case 21:{
                award.setExecuteCommands(!award.isExecuteCommands());
                player.openInventory(this.getInventory());
                SerializeUtils.saveRewardData();
                return;
            }
            //添加指令
            case 30:{
                ReceiveUtils.addCommand(player,this,award);
                SerializeUtils.saveRewardData();
                return;
            }
            //删除指令
            case 39:{
                if(award.getCommands().size()==0){
                    return;
                }
                ReceiveUtils.delCommand(player,this,award);
                SerializeUtils.saveRewardData();
                return;
            }
            //设置显示名称
            case 23:{
                ReceiveUtils.setAwardDisplayName(player,this,award);
                SerializeUtils.saveRewardData();
                return;
            }

            //上一层
            case 0: player.openInventory(returnGui.getInventory());break;
            //退出
            case 8: player.getOpenInventory().close();return;

            default:
        }
    }
}
