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
        inv.setItem(31,new MyItem(command).setDisplayName(LangUtils.Shop2)
                .setLore(LangUtils.Shop3)
                .getItem());
        inv.setItem(37,new MyItem(Material.ANVIL).setDisplayName(LangUtils.Shop4)
                .setLore(LangUtils.Shop5)
                .getItem());
        inv.setItem(38,new MyItem(Material.ANVIL).setDisplayName(LangUtils.Shop6)
                .setLore(LangUtils.Shop5)
                .getItem());
        inv.setItem(39,new MyItem(Material.ANVIL).setDisplayName(LangUtils.Shop7)
                .setLore(LangUtils.Shop5)
                .getItem());

        inv.setItem(41,new MyItem(Material.ANVIL).setDisplayName(LangUtils.Shop8)
                .setLore(LangUtils.Shop10)
                .getItem());
        inv.setItem(42,new MyItem(Material.ANVIL).setDisplayName(LangUtils.Shop9)
                .setLore(LangUtils.Shop10)
                .getItem());
        inv.setItem(43,new MyItem(Material.ANVIL).setDisplayName(LangUtils.Shop11)
                .setLore(LangUtils.Shop10)
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
                if (takeValue(lottery, player, amount * value)) {
                    GiveUtils.giveLottery(player, lottery.getName(), amount);
                    player.closeInventory();
                } else {
                    inv.setItem(31, new MyItem(command).setDisplayName(LangUtils.CantAfford).setLore(LangUtils.Shop12).getItem());
                }
                break;
            }
            default:
        }
    }

    private void updateData(){
        //现在购买的数量
        inv.setItem(13,new MyItem(Material.DIAMOND)
                .setDisplayName(LangUtils.Shop13)
                .setAmount(amount)
                .addLore(LangUtils.Shop14+amount)
                .addLore(LangUtils.Shop15+value*amount+sellType.getSellType())
                        .addLore(LangUtils.Shop16+getAccount()+sellType.getSellType())
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


    private int getMax(){
        int max = getAccount()/value;
        max = Math.max(1,max);
        max = Math.min(64,max);
        return max;
    }
}
