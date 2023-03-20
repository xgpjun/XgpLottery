package cn.xgp.xgplottery.Gui.Impl;


import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Utils.GuiItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

public class LotteryMenu extends LotteryGui {
    private Inventory Menu = Bukkit.createInventory(this,6*9,"这是默认GUI");
    private final String title = ChatColor.RED+"关注嘉然顿顿解馋";
    //TODO 文本自定义

    @Override
    @NotNull
    public Inventory getInventory() {
        return Menu;
    }

    public LotteryMenu(String title) {
        Menu = Bukkit.createInventory(this,6*9,title);
    }


    public LotteryMenu() {
        Menu = Bukkit.createInventory(this,6*9,title);
    }

    public LotteryGui LoadGui(){
        setBorder(Menu);
        //管理奖池
        GuiItem manageIcon = new GuiItem(Material.COMMAND_BLOCK);
        Menu.setItem(21,manageIcon.setDisplayName(ChatColor.RED+"管理奖池").setLore(ChatColor.YELLOW+"点击此处进入奖池管理页面").getItem());
        //新建奖池
        GuiItem createIcon = new GuiItem(Material.CHEST);
        Menu.setItem(23,createIcon.setDisplayName(ChatColor.RED+"创建奖池").setLore(ChatColor.YELLOW+"点击此处创建奖池").getItem());
        return this;
    }

}
