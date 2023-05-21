package cn.xgp.xgplottery.Gui.Impl.Shop;

import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.GiveUtils;
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

public class LotteryShop extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9,ChatColor.GOLD+"商店");

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
            String mt = maxTime>0? String.valueOf(maxTime) :"未设置保底次数";
            String sellType = lottery.isPoint()?ChatColor.AQUA+"点券":ChatColor.AQUA+"金币";
            String value = lottery.getValue()>0?ChatColor.GOLD+"价格："+ChatColor.AQUA+lottery.getValue():ChatColor.GOLD+"暂未开放售卖";
            inv.setItem(slot[index],new MyItem(Material.CHEST)
                    .setDisplayName(ChatColor.BLUE+"奖池 :"+ChatColor.AQUA + lottery.getName())
                    .setLore(ChatColor.GOLD+"保底次数："+ChatColor.RESET+""+ChatColor.GREEN +mt,
                            value,
                            ChatColor.GOLD+"货币类型："+sellType,
                            ChatColor.AQUA +"左键购买，右键打开奖池预览")
                    .getItem());

            index++;
            if(index==4*7)
                break;
        }
        setBorder(inv);
        String points = XgpLottery.ppAPI!=null? "点券:"+ChatColor.AQUA+ XgpLottery.ppAPI.look(player.getUniqueId()) :"本服未安装点券插件";
        String money = XgpLottery.eco!=null?"金币："+ChatColor.AQUA+ XgpLottery.eco.getBalance(player) :"本服未安装经济系统";
        inv.setItem(0,new MyItem(Material.DIAMOND)
                .setDisplayName(ChatColor.GOLD+"个人信息")
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
                player.sendMessage(ChatColor.GOLD+"[温馨提示]"+ChatColor.GREEN+ "这个奖池暂未开启售卖");
                return;
            }
            if(lottery.isPoint()){
                if(XgpLottery.ppAPI==null){
                    player.sendMessage(ChatColor.RED+"本服务器未加载点券系统，请联系管理员");
                    return;
                }
                if(XgpLottery.ppAPI.take(player.getUniqueId(),value)){
                    GiveUtils.giveLottery(player,lottery.getName());
                    player.openInventory(new LotteryShop(player).getInventory());
                    return;
                }
                player.closeInventory();
                player.sendMessage(ChatColor.RED+"您的点券余额不足");
            }
            else {
                if(XgpLottery.eco ==null){
                    player.sendMessage(ChatColor.RED+"本服务器未加载经济系统，请联系管理员");
                    return;
                }
                Economy econ = XgpLottery.eco;
                EconomyResponse r = econ.withdrawPlayer(player, value);
                if(r.transactionSuccess()) {
                    player.sendMessage(String.format(ChatColor.GREEN+"成功购买！你还有"+ChatColor.AQUA+" %s", econ.format(r.balance)));
                } else {
                    player.sendMessage(String.format(ChatColor.RED+"你的钱不够，你只有"+ChatColor.AQUA+"%s",r.balance));
                }
            }

        }

    }
}
