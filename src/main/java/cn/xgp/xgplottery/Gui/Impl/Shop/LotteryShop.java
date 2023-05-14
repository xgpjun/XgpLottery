package cn.xgp.xgplottery.Gui.Impl.Shop;

import cn.xgp.xgplottery.Gui.MyItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class LotteryShop extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9,ChatColor.GOLD+"商店");

    private Player player;

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
        inv.setItem(0,new MyItem(Material.GRAY_STAINED_GLASS_PANE)
                .setDisplayName(ChatColor.GRAY+"我也是有边界的>_<")
                .setLore(ChatColor.GRAY+ "这是分界线捏，没有别的东西了~")
                .getItem());

        String points = XgpLottery.ppAPI!=null? "点券:"+ChatColor.AQUA+ XgpLottery.ppAPI.look(player.getUniqueId()) :"本服未安装点券插件";
        String money = XgpLottery.eco!=null?"金币："+ChatColor.AQUA+ XgpLottery.eco.getBalance(player) :"本服未安装经济系统";
        inv.setItem(4,new MyItem(Material.DIAMOND)
                .setDisplayName(ChatColor.GOLD+"个人信息")
                .setLore(ChatColor.GOLD+money,ChatColor.GOLD+points)
                .addEnchant()
                .getItem());
        return this;
    }
}
