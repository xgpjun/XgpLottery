package cn.xgp.xgplottery.Gui.Impl.Shop;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Lottery.SellType;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.Utils.GiveUtils;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;


public class SetAmountGui extends PlayerGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9, ChatColor.GOLD+ LangUtils.ShopTitle);
    private final Lottery lottery;

    private final LotteryShop returnGui;
    private final Player player;
    private final int value;
    private int amount;
    private final SellType sellType;

    public SetAmountGui(@NotNull Lottery lottery, LotteryShop returnGui, Player player, int amount) {
        this.lottery = lottery;
        this.returnGui = returnGui;
        this.player = player;
        this.amount = amount;
        this.sellType = lottery.getSellType();
        this.value = lottery.getValue();
    }

    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        IntStream.range(0,54).forEach(i->inv.setItem(i,borderGlass));
        inv.setItem(0,new MyItem(Material.COMPASS)
                .setDisplayName(ChatColor.GREEN+LangUtils.PreviousInv1)
                .setLore(ChatColor.GOLD+ LangUtils.PreviousInv2)
                .getItem());
        inv.setItem(8,exit);
        //设置物品数量显示
        updateData();

        //确认交易
        inv.setItem(31,new MyItem(command).setDisplayName(ChatColor.GOLD+"成交！")
                .setLore("§7[§a √ §7] §b点击完成交易！")
                .getItem());
        inv.setItem(37,new MyItem(Material.ANVIL).setDisplayName(ChatColor.GOLD+"[最小]")
                .setLore("§7[§a-§7] §b点击减少购买数量")
                .getItem());
        inv.setItem(38,new MyItem(Material.ANVIL).setDisplayName(ChatColor.GOLD+"[-10]")
                .setLore("§7[§a-§7] §b点击减少购买数量")
                .getItem());
        inv.setItem(39,new MyItem(Material.ANVIL).setDisplayName(ChatColor.GOLD+"[-1]")
                .setLore("§7[§a-§7] §b点击减少购买数量")
                .getItem());

        inv.setItem(41,new MyItem(Material.ANVIL).setDisplayName(ChatColor.GOLD+"[+1]")
                .setLore("§7[§a+§7] §b点击增加购买数量")
                .getItem());
        inv.setItem(42,new MyItem(Material.ANVIL).setDisplayName(ChatColor.GOLD+"[+10]")
                .setLore("§7[§a+§7] §b点击增加购买数量")
                .getItem());
        inv.setItem(43,new MyItem(Material.ANVIL).setDisplayName(ChatColor.GOLD+"[最大]")
                .setLore("§7[§a+§7] §b点击增加购买数量")
                .getItem());
        return null;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0: player.openInventory(returnGui.refresh());break;
            //退出
            case 8: player.getOpenInventory().close();break;
            //最小
            case 37:{
                if(!ConfigSetting.giveLottery) return;
                amount = 1;
                updateData();
                break;
            }
            case 38:{
                if(!ConfigSetting.giveLottery) return;
                amount = Math.max(1,amount-10);
                updateData();
                break;
            }
            case 39:{
                if(!ConfigSetting.giveLottery) return;
                amount = Math.max(1,amount-1);
                updateData();
                break;
            }
            case 41:{
                if(!ConfigSetting.giveLottery) return;
                int count = Math.min(64,amount+1);
                if(canAfford(count)){
                    amount=count;
                    updateData();
                }else {
                    amount=getMax();
                    updateData();
                }
                break;
            }
            case 42:{
                if(!ConfigSetting.giveLottery) return;
                int count = Math.min(64,amount+10);
                if(canAfford(count)){
                    amount=count;
                    updateData();
                }else {
                    amount=getMax();
                    updateData();
                }
                break;
            }
            case 43:{
                if(!ConfigSetting.giveLottery) return;
                int count = 64;
                if(canAfford(count)){
                    amount=count;
                    updateData();
                }else {
                    amount=getMax();
                    updateData();
                }
                break;
            }
            case 31:{
                if(takeValue()){
                    GiveUtils.giveLottery(player, lottery.getName(), amount);
                    player.closeInventory();
                }else {
                    inv.setItem(31,new MyItem(command).setDisplayName(ChatColor.RED+"你买不起！").setLore("§7[§4 x §7] §b骚年，你不能这样做！").getItem());
                }
                break;
            }
            default:
        }
    }

    private void updateData(){
        //现在购买的数量
        inv.setItem(13,new MyItem(Material.DIAMOND)
                .setDisplayName(ChatColor.GOLD+"购买数量")
                .setAmount(amount)
                .addLore(ChatColor.BLUE+"数量: "+ChatColor.AQUA+amount)
                .addLore(ChatColor.BLUE+"花费: "+ChatColor.AQUA+value*amount+sellType.getSellType())
                        .addLore(ChatColor.BLUE+"你拥有: "+ChatColor.AQUA+getAccount()+sellType.getSellType())
                .getItem());
    }

    private int getAccount(){
        switch (sellType){
            case POINTS: {
                return XgpLottery.ppAPI.look(player.getUniqueId());
            }
            case MONEY:{
                return (int) XgpLottery.eco.getBalance(player);
            }
            case EXP:{
                return player.getLevel();
            }
        }
        return 0;
    }
    private boolean canAfford(int count){
        int cost = count*value;
        return cost<=getAccount();
    }

    private boolean takeValue(){
        int cost = amount*value;
        if (!ConfigSetting.giveLottery)
            cost = value;
        switch (sellType){
            case POINTS: {
                return XgpLottery.ppAPI.take(player.getUniqueId(),cost);
            }
            case MONEY:{
                EconomyResponse r = XgpLottery.eco.withdrawPlayer(player, cost);
                return r.transactionSuccess();
            }
            case EXP:{
                int rawLevel = player.getLevel();
                if(rawLevel<cost)
                    return false;
                player.setLevel(rawLevel-cost);
                return true;
            }
        }
        return false;
    }

    private int getMax(){
        int max = getAccount()/value;
        max = Math.max(1,max);
        max = Math.min(64,max);
        return max;
    }
}
