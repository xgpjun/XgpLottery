package cn.xgp.xgplottery.Gui.Impl.Shop;

import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.GiveUtils;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class LotteryShop extends PlayerGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9,ChatColor.GOLD+LangUtils.ShopTitle);

    private final Player player;

    public LotteryShop(Player player){
        this.player = player;
    }

    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        //最大4*7
        int index = 0;
        for(Lottery lottery: XgpLottery.lotteryList.values()){
            int maxTime = lottery.getMaxTime();
            String mt = maxTime>0? String.valueOf(maxTime) : LangUtils.NotSetMaxTime;
            String sellType = lottery.isPoint()?ChatColor.AQUA+LangUtils.Points:ChatColor.AQUA+LangUtils.Money;
            String value = lottery.getValue()>0?ChatColor.GOLD+ LangUtils.Price +ChatColor.AQUA+lottery.getValue():ChatColor.GOLD+LangUtils.NotSale;
            inv.setItem(slot[index],new MyItem(Material.CHEST)
                    .setDisplayName(ChatColor.BLUE+LangUtils.BoxInformation3+ChatColor.AQUA + lottery.getName())
                    .setLore(ChatColor.GOLD+LangUtils.BoxInformation4+ChatColor.RESET+""+ChatColor.GREEN +mt,
                            value,
                            ChatColor.GOLD+LangUtils.SaleType +sellType,
                            ChatColor.AQUA +LangUtils.SaleOperation)
                    .getItem());

            index++;
            if(index==4*7)
                break;
        }
        setBorder(inv);
        String points = XgpLottery.ppAPI!=null? LangUtils.Points+ChatColor.AQUA+ XgpLottery.ppAPI.look(player.getUniqueId()) :LangUtils.NoMoneyAPI;
        String money = XgpLottery.eco!=null?LangUtils.Money+ChatColor.AQUA+ XgpLottery.eco.getBalance(player) :LangUtils.NoPointsAPI;
        inv.setItem(0,new MyItem(Material.DIAMOND)
                .setDisplayName(ChatColor.GOLD+LangUtils.PersonalInformation)
                .setLore(ChatColor.GOLD+money,ChatColor.GOLD+points)
                .addEnchant()
                .getItem());
        return this;
    }
    public void handleClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getRawSlot()==8){
            player.getOpenInventory().close();
            return;
        }
        ItemStack item = e.getCurrentItem();
        if(item ==null||!item.getType().equals(Material.CHEST))
            return;
        ItemMeta mata = item.getItemMeta();
        if(mata==null)
            return;
        Lottery lottery = XgpLottery.lotteryList.get(mata.getDisplayName().split("§b")[1]);
        if (e.isLeftClick() && !e.isShiftClick()) {
            //未开启售卖
            int value = lottery.getValue();
            if(value<=0){
                player.sendMessage(ChatColor.GOLD+LangUtils.LotteryPrefix+ChatColor.GREEN+ LangUtils.NotSale);
                return;
            }
            if(lottery.isPoint()){
                if(XgpLottery.ppAPI==null){
                    player.sendMessage(ChatColor.RED+LangUtils.NoMoneyAPI);
                    return;
                }
                if(XgpLottery.ppAPI.take(player.getUniqueId(),value)){
                    GiveUtils.giveLottery(player,lottery.getName());
                    player.sendMessage(String.format(ChatColor.GREEN+ LangUtils.CanAfford +ChatColor.AQUA+" %s", XgpLottery.ppAPI.look(player.getUniqueId())));
                    player.openInventory(new LotteryShop(player).getInventory());
                    return;
                }
                player.closeInventory();
                player.sendMessage(ChatColor.RED+LangUtils.CantAfford);
            }
            else {
                if(XgpLottery.eco ==null){
                    player.sendMessage(ChatColor.RED+LangUtils.NoPointsAPI);
                    return;
                }
                Economy econ = XgpLottery.eco;
                EconomyResponse r = econ.withdrawPlayer(player, value);
                if(r.transactionSuccess()) {
                    player.sendMessage(String.format(ChatColor.GREEN+ LangUtils.CanAfford +ChatColor.AQUA+" %s", econ.format(r.balance)));
                    GiveUtils.giveLottery(player,lottery.getName());
                    player.openInventory(new LotteryShop(player).getInventory());

                } else {
                    player.sendMessage(ChatColor.RED+LangUtils.CantAfford);
                    player.closeInventory();
                }
            }
        }
    }
}
