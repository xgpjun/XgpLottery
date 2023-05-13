package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryCreateGui;
import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryManageGui;
import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryMenuGui;
import cn.xgp.xgplottery.Gui.MyItem;
import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolGui;
import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolShow;
import cn.xgp.xgplottery.Gui.Impl.Pool.SpecialPoolGui;
import cn.xgp.xgplottery.Gui.Impl.Pool.SpecialPoolShow;
import cn.xgp.xgplottery.Gui.Impl.Shop.LotteryShop;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiListener implements Listener {

    @EventHandler
    public void menuGUI(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getInventory().getHolder()==null)
            return;
        if(e.getInventory().getHolder() instanceof LotteryMenuGui){
            if(e.getRawSlot()>=0&&e.getRawSlot()<=53){
                e.setCancelled(true);
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
    }

    @EventHandler
    public void createGui(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getInventory().getHolder()==null)
            return;
        if(e.getInventory().getHolder() instanceof LotteryCreateGui){
            if(e.getRawSlot()>=0&&e.getRawSlot()<=53){
                e.setCancelled(true);
                switch (e.getRawSlot()){
                    //上一层
                    case 0: player.openInventory(new LotteryMenuGui().getInventory());break;
                    //退出
                    case 8: player.getOpenInventory().close();break;
                    case 22: Lottery.createLottery(player); break;
                    default:
                }
            }
        }
    }
    //奖池管理
    @EventHandler
    public void ManageGui(InventoryClickEvent e){
        if(e.getInventory().getHolder()==null)
            return;
        if(e.getInventory().getHolder() instanceof LotteryManageGui){
            if(e.getRawSlot()>=0&&e.getRawSlot()<=53){
                Player player = (Player) e.getWhoClicked();
                e.setCancelled(true);
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

                        } else if(e.isLeftClick()&&!e.isShiftClick()){
                            player.openInventory(new LotteryPoolGui(lottery).getInventory());
                        }else if(e.isRightClick()&&!e.isShiftClick()){
                            player.openInventory(new SpecialPoolGui(lottery).getInventory());
                        }
                    }
                }
            }
        }
    }
    //奖池内容
    @EventHandler
    public void poolGui(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getInventory().getHolder()==null)
            return;
        if(e.getInventory().getHolder() instanceof LotteryPoolGui){
            if(e.getRawSlot()>=0&&e.getRawSlot()<=53) {
                e.setCancelled(true);
                if(e.getRawSlot()>=0&&e.getRawSlot()<=44&&e.getCurrentItem()!=null){
                    //得到奖池对象
                    Lottery lottery = ((LotteryPoolGui) e.getInventory().getHolder()).getLottery();
                    //被点击的物品
                    int index = e.getSlot();
                    ItemStack itemStack = lottery.getItems().get(index);;
                    //删除物品
                    if(e.isShiftClick()&&e.isRightClick()&&itemStack!=null){
                        if(lottery.getItems().contains(itemStack)){
                            lottery.delItem(e.getRawSlot());
                            player.openInventory(new LotteryPoolGui(lottery).getInventory());
                            SerializeUtils.saveLotteryData();
                        }
                    }else if(!e.isShiftClick()&&e.isLeftClick()&&itemStack!=null){
                        Lottery.receiveWeight(player,lottery,itemStack,e.getRawSlot(),false);
                    }
                }
                //退出/添加物品
                else if(e.isLeftClick()&&e.getRawSlot()==49){
                    if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                        // 玩家拿起了物品，执行相关操作
                        ItemStack cursorItem = e.getCursor();
                        ItemStack item = new ItemStack(cursorItem);
                        // 把玩家拿起的物品放入背包
                        player.setItemOnCursor(null);
                        player.getInventory().addItem(cursorItem);
                        Lottery lottery = ((LotteryPoolGui) e.getInventory().getHolder()).getLottery();
                        if(lottery.getItems().size()<45){
                            lottery.addItem(item);
                            player.openInventory(new LotteryPoolGui(lottery).getInventory());
                            SerializeUtils.saveLotteryData();
                        }else {
                            player.sendMessage(ChatColor.RED+"奖池已满 ，请删除一些后继续添加");
                            player.closeInventory();
                        }
                    }
                    else {
                        // 玩家没有拿起物品，处理点击铁砧返回
                        player.openInventory(new LotteryManageGui().getInventory());
                    }
                }
            }
        }
    }
    //保底物品
    @EventHandler
    public void spItemGui(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getInventory().getHolder()==null)
            return;
        if(e.getInventory().getHolder() instanceof SpecialPoolGui){
            if(e.getRawSlot()>=0&&e.getRawSlot()<=53) {
                e.setCancelled(true);
                if(e.getRawSlot()>=0&&e.getRawSlot()<=44&&e.getCurrentItem()!=null){
                    //得到奖池对象
                    Lottery lottery = ((SpecialPoolGui) e.getInventory().getHolder()).getLottery();
                    //被点击的物品
                    int index = e.getSlot();
                    ItemStack itemStack = lottery.getSpItems().get(index);
                    //删除物品
                    if(e.isShiftClick()&&e.isRightClick()&&itemStack!=null){
                        if(lottery.getSpItems().contains(itemStack)){
                            lottery.delSpItem(e.getRawSlot());
                            player.openInventory(new SpecialPoolGui(lottery).getInventory());
                            SerializeUtils.saveLotteryData();
                        }
                    }else if(!e.isShiftClick()&&e.isLeftClick()&&itemStack!=null){
                        Lottery.receiveWeight(player,lottery,itemStack,e.getRawSlot(),true);
                    }
                }
                //退出/添加物品
                else if(e.isLeftClick()&&e.getRawSlot()==49){
                    if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                        // 玩家拿起了物品，执行相关操作
                        ItemStack cursorItem = e.getCursor();
                        ItemStack item = new ItemStack(cursorItem);
                        // 把玩家拿起的物品放入背包
                        player.setItemOnCursor(null);
                        player.getInventory().addItem(cursorItem);
                        Lottery lottery = ((SpecialPoolGui) e.getInventory().getHolder()).getLottery();
                        if(lottery.getSpItems().size()<45){
                            lottery.addSpItem(item);
                            player.openInventory(new SpecialPoolGui(lottery).getInventory());
                            SerializeUtils.saveLotteryData();
                        }else {
                            player.sendMessage(ChatColor.RED+"奖池已满 ，请删除一些后继续添加");
                            player.closeInventory();
                        }
                    }
                    else {
                        // 玩家没有拿起物品，处理点击铁砧返回
                        player.openInventory(new LotteryManageGui().getInventory());
                    }
                }
            }
        }
    }



    @EventHandler
    public void poolShow(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getInventory().getHolder()==null)
            return;
        if(e.getInventory().getHolder() instanceof LotteryPoolShow) {
            e.setCancelled(true);
            if(e.isLeftClick()&&e.getRawSlot()==49){
                Lottery lottery = ((LotteryPoolShow) e.getInventory().getHolder()).getLottery();
                player.openInventory(new SpecialPoolShow(lottery).getInventory());
            }
        }
    }
    @EventHandler
    public void spPoolShow(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getInventory().getHolder()==null)
            return;
        if(e.getInventory().getHolder() instanceof SpecialPoolShow) {
            e.setCancelled(true);
            if(e.isLeftClick()&&e.getRawSlot()==49){
                Lottery lottery = ((SpecialPoolShow) e.getInventory().getHolder()).getLottery();
                player.openInventory(new LotteryPoolShow(lottery).getInventory());
            }
        }
    }

    //商店页面
    @EventHandler
    public void shopGui(InventoryClickEvent e){
        if(e.getInventory().getHolder()==null)
            return;
        if(e.getInventory().getHolder() instanceof LotteryShop){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if(e.getRawSlot()==8){
                player.getOpenInventory().close();
            }else{
                ItemStack item = e.getCurrentItem();
                if(item !=null&&item.getType().equals(Material.CHEST)){
                    ItemMeta mata = item.getItemMeta();
                    if(mata!=null) {
                        Lottery lottery = XgpLottery.lotteryList.get(mata.getDisplayName().split("§b")[1]);
                        if (e.isLeftClick() && !e.isShiftClick()) {
                            //TODO 购买物品的逻辑
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void dragItem(InventoryDragEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getInventory().getHolder() == null)
            return;
        if(!player.isOp()&&e.getInventory().getHolder() instanceof LotteryGui){
            e.setCancelled(true);
        }
    }


}
