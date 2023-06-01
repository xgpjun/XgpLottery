package cn.xgp.xgplottery.Gui.Impl.Select;


import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryManageGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class SelectAnimation extends LotteryGui {
    private final Lottery lottery;
    private final Inventory inv = Bukkit.createInventory(this,6*9, ChatColor.GOLD+ LangUtils.Select );

    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }
    @Override
    public LotteryGui loadGui() {
        setBorder(inv);
        inv.setItem(10,new MyItem(Material.CHEST)
                .setDisplayName(ChatColor.AQUA+LangUtils.ScrollingAnimation)
                .getItem());
        inv.setItem(11,new MyItem(Material.CHEST)
                .setDisplayName(ChatColor.AQUA+ LangUtils.SelectItemAnimation)
                .getItem());

        return this;
    }
    public void handleClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0: player.openInventory(new LotteryManageGui().getInventory());break;
            //退出
            case 8: player.getOpenInventory().close();break;
            case 10: {
                lottery.setAnimation("BoxAnimation");
                SerializeUtils.saveLotteryData();
                player.openInventory(new LotteryManageGui().getInventory());
                break;
            }
            case 11: {
                lottery.setAnimation("SelectItemAnimation");
                SerializeUtils.saveLotteryData();
                player.openInventory(new LotteryManageGui().getInventory());
                break;
            }
            default:
        }

    }
}
