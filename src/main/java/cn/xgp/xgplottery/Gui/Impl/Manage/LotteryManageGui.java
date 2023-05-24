package cn.xgp.xgplottery.Gui.Impl.Manage;

import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolGui;
import cn.xgp.xgplottery.Gui.Impl.Pool.SpecialPoolGui;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
//管理奖池GUI
public class LotteryManageGui extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9,ChatColor.GOLD+"奖池管理");

    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        //最大4*7
        int index = 0;
        for(Lottery lottery:XgpLottery.lotteryList.values()){
            int maxTime = lottery.getMaxTime();
            String mt = maxTime>0? String.valueOf(maxTime) :"未设置保底次数";
            String sellType = lottery.isPoint()?ChatColor.AQUA+"点券":ChatColor.AQUA+"金币";
            inv.setItem(slot[index],new MyItem(Material.CHEST)
                    .setDisplayName(ChatColor.BLUE+"奖池 :"+ChatColor.AQUA + lottery.getName())
                    .setLore(ChatColor.GOLD+"保底次数："+ChatColor.RESET+""+ChatColor.GREEN +mt,
                            ChatColor.GOLD+"货币类型："+ChatColor.AQUA+sellType +ChatColor.GOLD+"价格："+ChatColor.AQUA+lottery.getValue(),
                            ChatColor.GOLD+"抽奖动画："+ChatColor.AQUA+lottery.getAnimationObject(null,null,true).toLore(),
                            ChatColor.AQUA +"shift+左键点击设置保底 shift+右键设置价格" ,
                            ChatColor.AQUA +"左键打开奖池，右键打开保底池")
                    .getItem());

            index++;
            if(index==4*7)
                break;
        }
        setBorder(inv);
        return this;
    }
    public void handleClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0: player.openInventory(new LotteryMenuGui().getInventory());break;
            //退出
            case 8: player.getOpenInventory().close();break;
            default:
        }
        ItemStack item = e.getCurrentItem();
        if(item !=null&&item.getType().equals(Material.CHEST)){
            ItemMeta mata = item.getItemMeta();
            if(mata!=null){
                Lottery lottery = XgpLottery.lotteryList.get(mata.getDisplayName().split("§b")[1]);
                //设置保底数
                if(e.isShiftClick()&&e.isLeftClick()){
                    Lottery.setMaxTime(player,lottery);
                }else if(e.isShiftClick()&&e.isRightClick()){
                    Lottery.setValue(player,lottery);
                } else if(e.isLeftClick()&&!e.isShiftClick()){
                    player.openInventory(new LotteryPoolGui(lottery).getInventory());
                }else if(e.isRightClick()&&!e.isShiftClick()){
                    player.openInventory(new SpecialPoolGui(lottery).getInventory());
                }
            }
        }
    }
}
