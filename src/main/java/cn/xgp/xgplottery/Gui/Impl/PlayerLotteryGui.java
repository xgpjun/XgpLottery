package cn.xgp.xgplottery.Gui.Impl;

import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolShow;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class PlayerLotteryGui extends PlayerGui {
    final Inventory inv = Bukkit.createInventory(this,6*9, LangUtils.PlayerGuiTitle);
    Player player;
    Lottery lottery;
    boolean keyOrTicket;
    boolean single = false;
    boolean multiple = false;

    public PlayerLotteryGui(Player player, Lottery lottery, boolean keyOrTicket) {
        this.player = player;
        this.lottery = lottery;
        this.keyOrTicket = keyOrTicket;
    }

    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        ItemStack item = VersionAdapterUtils.getItemInMainHand(player);
        int amount = 0;
        if (item != null && item.getItemMeta() != null) {
            if (NMSUtils.checkTag(item, keyOrTicket, lottery.getName())) {
                amount = item.getAmount();
            }
        }
        setBorder(inv);
        String lore =LangUtils.PlayerGui1;
        if (ConfigSetting.giveLottery) {
            //判断个数
            if (amount > 0) {
                lore = LangUtils.PlayerGui2;
                single = true;
            }
        } else if(lottery.getValue()>0) {
            //售卖
            lore = LangUtils.PlayerGui3;
            single = true;

        }
        if (lottery.getLimitedTimes() > 0) {
            if (TimesUtils.getTimes(player.getUniqueId(), lottery.getName()) >= lottery.getLimitedTimes()) {
                lore = LangUtils.PlayerGui4;
                single = false;
            }
        }
        //判断背包是否为满
        int emptySlots = VersionAdapterUtils.getPlayerEmptySlot(player);
        if (lottery.isCheckFull()) {
            if (emptySlots == 0) {
                single = false;
                lore = LangUtils.PlayerGui5;
            }
        }


        inv.setItem(20, new MyItem(Material.CHEST).setDisplayName(LangUtils.PlayerGui6).addLore(lore)
                .addEnchant().getItem());


        lore = LangUtils.PlayerGui7;

        if (ConfigSetting.giveLottery) {
            //判断个数
            if (amount >= 10) {
                lore = LangUtils.PlayerGui8;
                multiple = true;
            }
        } else if(lottery.getValue()>0){
            lore = LangUtils.PlayerGui9;
            multiple = true;
        }


        if (lottery.getLimitedTimes() > 0) {
            if (TimesUtils.getTimes(player.getUniqueId(), lottery.getName()) + 10 > lottery.getLimitedTimes()) {
                lore = LangUtils.PlayerGui10;
                multiple = false;
            }
        }
        //判断背包是否为满
        if (lottery.isCheckFull()) {
            if (emptySlots <= 9) {
                multiple = false;
                lore = LangUtils.PlayerGui11;
            }
        }

        inv.setItem(24, new MyItem(Material.CHEST).setDisplayName(LangUtils.PlayerGui12).addLore(lore)
                .addEnchant().getItem());
        String str;
        if (lottery.getMaxTime() > 0) {
            int times = 0;
            if (TimesUtils.getCurrentLotteryTimes(player.getUniqueId(), lottery.getName()) != null)
                times = TimesUtils.getCurrentLotteryTimes(player.getUniqueId(), lottery.getName()).getTimes();
            str = ChatColor.BLUE + LangUtils.BoxInformation4 + ChatColor.AQUA + lottery.getMaxTime() + ChatColor.BLUE + LangUtils.BoxInformation5 + ChatColor.AQUA + times;
        } else
            str = ChatColor.BLUE + LangUtils.BoxInformation6;
        inv.setItem(22,new MyItem(Material.DIAMOND).setDisplayName(LangUtils.PlayerGui13)
                .addLore(LangUtils.PlayerGui14)
                .addLore(LangUtils.PlayerGui15+amount)
                .addLore(str)
                .getItem());


        inv.setItem(29,new MyItem(Material.CHEST).setDisplayName(LangUtils.PlayerGui16)
                .setLore(LangUtils.PlayerGui19).getItem());
        inv.setItem(33,new MyItem(Material.CHEST).setDisplayName(LangUtils.PlayerGui17)
                .setLore(LangUtils.PlayerGui19).getItem());
        inv.setItem(31,new MyItem(Material.PAPER).setDisplayName(LangUtils.PlayerGui18)
                .setLore(LangUtils.PlayerGui19).getItem());
        return this;
    }

    int eggs = 0;
    @Override
    public void handleClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0:
            //退出
            case 8: player.getOpenInventory().close();break;
            case 29: player.openInventory(new LotteryPoolShow(lottery,lottery.getAwards(),this).getInventory()); break;
            case 33: player.openInventory(new LotteryPoolShow(lottery,lottery.getSpAwards(),this).getInventory()); break;
            case 20: {
                loadGui();
                if (!single) {
                    return;
                }
                if (!ConfigSetting.giveLottery) {
                    if (takeValue(lottery, player, lottery.getValue())) {
                        lottery.open(player, true, false);
                    } else {
                        player.closeInventory();
                        player.sendMessage(ChatColor.GOLD + LangUtils.LotteryPrefix + LangUtils.CantAfford);
                    }
                } else {
                    lottery.open(player, false, false);
                }
                break;
            }
            case 24: {
                loadGui();
                if (!multiple) {
                    return;
                }
                if (!ConfigSetting.giveLottery) {
                    if (takeValue(lottery, player, lottery.getValue() * 10)) {
                        lottery.open(player, true, true);
                    } else {
                        player.closeInventory();
                        player.sendMessage(ChatColor.GOLD + LangUtils.LotteryPrefix + LangUtils.CantAfford);
                    }
                } else {
                    lottery.open(player, false, true);
                }
                break;
            }
            case 22:{
                eggs++;
                if(eggs==10){
                    inv.setItem(22,new MyItem(command).setDisplayName(ChatColor.GOLD+"小钢炮最可爱.jpg").setLore(ChatColor.BLUE+"This is an Easter egg!").getItem());
                    eggs=0;
                }
                break;
            }
            case 31:{
                player.openInventory(new RecordGui(player,lottery.getName(),this).getInventory());
                break;
            }
            default:
        }
    }
}
