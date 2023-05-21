package cn.xgp.xgplottery.Gui.Impl.Manage;


import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.nmsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
//输入menu之后显示的管理菜单
public class LotteryMenuGui extends LotteryGui {
    private final Inventory Menu = Bukkit.createInventory(this,6*9,"XgpLottery管理菜单");

    @Override
    @NotNull
    public Inventory getInventory() {
        loadGui();
        return Menu;
    }
    public LotteryGui loadGui(){
        setBorder(Menu);
        //管理奖池
        Material command;
        if(nmsUtils.versionToInt<13){
            command = Material.valueOf("COMMAND");
        }else {
            command = Material.COMMAND_BLOCK;
        }

        MyItem manageButton = new MyItem(command);
        Menu.setItem(20,manageButton
                .setDisplayName(ChatColor.RED+"管理奖池")
                .setLore(ChatColor.YELLOW+"点击此处进入奖池管理页面")
                .getItem());
        //新建奖池
        MyItem createButton = new MyItem(Material.ANVIL);
        Menu.setItem(22,createButton
                .setDisplayName(ChatColor.RED+"创建奖池")
                .setLore(ChatColor.YELLOW+"点击此处创建奖池")
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
                        .setDisplayName(ChatColor.GRAY+"返回上一层")
                        .setLore(ChatColor.RED+ "你不能再返回上一层辣！ 没有辣！")
                        .getItem());
                break;
            }
            //退出
            case 8: player.getOpenInventory().close();break;
            //打开管理界面
            case 20: player.openInventory(new LotteryManageGui().getInventory());break;
            //打开创建界面
            case 22: player.openInventory(new LotteryCreateGui().getInventory());break;
            default:
        }
    }

}
