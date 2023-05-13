package cn.xgp.xgplottery.Gui.Impl.Manage;


import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.GuiItem;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
//输入menu之后显示的管理菜单
public class LotteryMenuGui extends LotteryGui {
    private final Inventory Menu = Bukkit.createInventory(this,6*9,"XgpLottery管理菜单");
    private final String title = ChatColor.RED+"关注嘉然顿顿解馋";
    //TODO 文本自定义

    @Override
    @NotNull
    public Inventory getInventory() {
        loadGui();
        return Menu;
    }


    public LotteryMenuGui() {

    }

    public LotteryGui loadGui(){
        setBorder(Menu);
        //管理奖池
        GuiItem manageButton = new GuiItem(Material.COMMAND_BLOCK);
        Menu.setItem(20,manageButton
                .setDisplayName(ChatColor.RED+"管理奖池")
                .setLore(ChatColor.YELLOW+"点击此处进入奖池管理页面")
                .getItem());
        //新建奖池
        GuiItem createButton = new GuiItem(Material.ANVIL);
        Menu.setItem(22,createButton
                .setDisplayName(ChatColor.RED+"创建奖池")
                .setLore(ChatColor.YELLOW+"点击此处创建奖池")
                .getItem());
        return this;
    }

}
