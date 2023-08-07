package cn.xgp.xgplottery.Gui.Impl.Shop;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LotteryShop extends PlayerGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9,ChatColor.GOLD+LangUtils.ShopTitle);

    int page;
    int size;
    private final Player player;

    public LotteryShop(Player player){
        this.player = player;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return getPage(1);
    }

    @Override
    public LotteryGui loadGui() {
        setBorder(inv);
        inv.setItem(45,previousPage);
        inv.setItem(53,nextPage);
        return this;
    }

    public Inventory refresh(){
        return getPage(page);
    }

    public Inventory getPage(int page){
        size =(int) Math.ceil( (double)XgpLottery.lotteryList.size() / 28);
        this.page = Math.max(1, Math.min(page, size));
        inv.clear();
        loadGui();


        int index = 0;
        List<Lottery> list = new ArrayList<>(XgpLottery.lotteryList.values());
        for (int i = (this.page - 1) * 28; i<list.size(); i++){
            Lottery lottery = list.get(i);
            String sellType = ChatColor.AQUA+lottery.getSellType().getSellType();
            String value = lottery.getValue()>0?ChatColor.GOLD+ LangUtils.Price +" :"+ChatColor.AQUA+lottery.getValue():ChatColor.GOLD+LangUtils.NotSale;

            inv.setItem(slot[index],new MyItem(Material.CHEST).setDisplayName(ChatColor.BLUE+LangUtils.PoolButton1+ChatColor.AQUA + lottery.getName())
                    .setLore(value,
                            ChatColor.GOLD + LangUtils.SaleType + " :" + sellType,
                            LangUtils.Shop1)
                    .getItem());
            index++;
            if (index == 4 * 7)
                break;
        }

        //显示服务器经济系统信息
        String points = XgpLottery.ppAPI != null ? LangUtils.Points + ChatColor.AQUA + ": " + XgpLottery.ppAPI.look(player.getUniqueId()) : LangUtils.NoMoneyAPI;
        String money = XgpLottery.eco != null ? LangUtils.Money + ChatColor.AQUA + ": " + XgpLottery.eco.getBalance(player) : LangUtils.NoPointsAPI;
        inv.setItem(0, new MyItem(Material.DIAMOND)
                .setDisplayName(ChatColor.GOLD + LangUtils.PersonalInformation)
                .setLore(ChatColor.GOLD + money,
                        ChatColor.GOLD + points)
                .addLore(ChatColor.BLUE + "XP: " + ChatColor.AQUA + player.getLevel())
                .addEnchant()
                .getItem());
        return inv;
    }

    public void handleClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0: break;
            //退出
            case 8: player.getOpenInventory().close();break;
            case 45:e.getWhoClicked().openInventory(getPage(this.page-1));break;
            case 53:e.getWhoClicked().openInventory(getPage(this.page+1));break;
            default:
        }

        ItemStack item = e.getCurrentItem();
        if(item !=null&&item.getType().equals(Material.CHEST)) {
            int index = findSlot(e.getRawSlot());
            if (index == -1)
                return;
            Lottery lottery = new ArrayList<>(XgpLottery.lotteryList.values()).get((this.page - 1) * 28 + index);
            if (isSelling(lottery)) {
                if (ConfigSetting.giveLottery)
                    player.openInventory(new SetAmountGui(lottery, this, player, 1).getInventory());
                else {
                    if (takeValue(lottery, player, lottery.getValue())) {
                        lottery.open(player, true, false);
                    }
                }

            }

        }
    }

    private boolean isSelling(Lottery lottery){
        if(lottery.getValue()<=0)
            return false;
        switch (lottery.getSellType()){
            case POINTS:{
                return XgpLottery.ppAPI!=null;
            }
            case MONEY:{
                return XgpLottery.eco!=null;
            }
            case EXP:
            default: return true;
        }
    }
}
