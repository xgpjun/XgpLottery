package cn.xgp.xgplottery.Gui.Impl.Manage;


import cn.xgp.xgplottery.Gui.Impl.Reward.RewardGui;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

//输入menu之后显示的管理菜单
public class LotteryMenuGui extends LotteryGui {
    private final Inventory Menu = Bukkit.createInventory(this,6*9,ChatColor.GOLD+ LangUtils.MenuTitle);

    @Override
    @NotNull
    public Inventory getInventory() {
        loadGui();
        return Menu;
    }
    public LotteryGui loadGui(){
        setBorder(Menu);
        //管理奖池

        MyItem manageButton = new MyItem(command);
        Menu.setItem(20,manageButton
                .setDisplayName(ChatColor.RED+ LangUtils.ManageButton1)
                .setLore(ChatColor.YELLOW+LangUtils.ManageButton2)
                .getItem());
        //新建奖池
        MyItem createButton = new MyItem(Material.ANVIL);
        Menu.setItem(22,createButton
                .setDisplayName(ChatColor.RED+LangUtils.CreateButton1)
                .setLore(ChatColor.YELLOW+LangUtils.CreateButton2)
                .getItem());

        Menu.setItem(24,new MyItem(Material.CHEST).setDisplayName(LangUtils.Reward1)
                .addLore(LangUtils.Reward2)
                .getItem());

        return this;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder()==null)
            return;
        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0:{
                e.getInventory().setItem(0,new MyItem(Material.COMPASS)
                        .setDisplayName(ChatColor.GRAY+LangUtils.PreviousInv1)
                        .setLore(ChatColor.RED+ LangUtils.PreviousInv3)
                        .getItem());
                break;
            }
            //退出
            case 8: player.getOpenInventory().close();break;
            //打开管理界面
            case 20: player.openInventory(new LotteryManageGui().getInventory());break;
            //打开创建界面
            case 22: player.openInventory(new LotteryCreateGui().getInventory());break;
            case 24: player.openInventory(new RewardGui().getInventory()); break;
            default:
        }
    }

}
