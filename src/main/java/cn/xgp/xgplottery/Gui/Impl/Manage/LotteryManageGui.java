package cn.xgp.xgplottery.Gui.Impl.Manage;

import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
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

//奖池列表
public class LotteryManageGui extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9,ChatColor.GOLD+LangUtils.ManageButton1);
    int page;
    int size;
    @Override
    public @NotNull Inventory getInventory() {
        return getPage(1);
    }

    @Override
    public LotteryGui loadGui() {
        //最大4*7
        setBorder(inv);
        inv.setItem(45,previousPage);
        inv.setItem(53,nextPage);
        return this;
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
            inv.setItem(slot[index],new MyItem(Material.CHEST)
                    .setDisplayName(ChatColor.BLUE+LangUtils.PoolButton1+ChatColor.AQUA + lottery.getName())
                    .setLore(ChatColor.BLUE+ "点击进入设置")
                    .getItem());
            index++;
            if(index==4*7)
                break;
        }
        return inv;
    }
    public void handleClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0: player.openInventory(new LotteryMenuGui().getInventory());break;
            //退出
            case 8: player.getOpenInventory().close();break;
            case 45:e.getWhoClicked().openInventory(getPage(this.page-1));break;
            case 53:e.getWhoClicked().openInventory(getPage(this.page+1));break;
            default:
        }
        ItemStack item = e.getCurrentItem();
        if(item !=null&&item.getType().equals(Material.CHEST)){
            int index = findSlot(e.getRawSlot());
            if(index==-1)
                return;
            Lottery lottery = new ArrayList<>(XgpLottery.lotteryList.values()).get((this.page-1)*28+index) ;
            player.openInventory(new LotterySetting(lottery.getName()).getInventory());

        }
    }
}
