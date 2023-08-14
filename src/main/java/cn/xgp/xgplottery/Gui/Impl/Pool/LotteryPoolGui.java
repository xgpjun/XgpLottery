package cn.xgp.xgplottery.Gui.Impl.Pool;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotterySetting;
import cn.xgp.xgplottery.Gui.Impl.Manage.LotterySettings.AwardSetting;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PoolGui;
import cn.xgp.xgplottery.Lottery.Award;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.ReceiveUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

//奖池详细内容
public class LotteryPoolGui extends PoolGui {
    private final Inventory inv;
    private final Lottery lottery;
    private List<Award> awards;
    int page;
    int size;

    public LotteryPoolGui(List<Award> awards,Lottery lottery){
        this.awards = awards;
        this.lottery = lottery;
        inv = Bukkit.createInventory(this,6*9, ChatColor.YELLOW+LangUtils.AwardList);
        size =  (int) Math.ceil( (double)awards.size() / 45);
    }
    @Override
    public @NotNull Inventory getInventory() {
        return getPage(1);
    }

    @Override
    public LotteryGui loadGui() {
        for (int index = 45;index<=53;index++){
            inv.setItem(index,borderGlass);
        }
        inv.setItem(45,previousPage);
        inv.setItem(53,nextPage);
        inv.setItem(47,new MyItem(Material.HOPPER).setDisplayName(LangUtils.SortAscending).getItem());
        inv.setItem(51,new MyItem(Material.HOPPER).setDisplayName(LangUtils.SortDescending).getItem());
        return this;
    }

    public Inventory getPage(int page){
        this.page = Math.max(1, Math.min(page, size));
        inv.clear();
        loadGui();
        inv.setItem(49,new MyItem(Material.DIAMOND)
                .setDisplayName(ChatColor.GOLD+LangUtils.CurrentPage+ChatColor.AQUA+ this.page)
                .addLore(ChatColor.BLUE+LangUtils.TotalPage+ChatColor.AQUA+ this.size)
                .addLore(ChatColor.BLUE+LangUtils.AnvilText1)
                .addLore(ChatColor.RED+LangUtils.AnvilText2)
                .addLore(ChatColor.GOLD+LangUtils.AnvilText5)
                .getItem());
        if(awards.size()==0)
            return inv;


        DecimalFormat df = new DecimalFormat("0.00%");

        for (int i=0;i<45;i++) {
            int index = i+(this.page-1)*45;
            if(index>=awards.size())
                break;
            Award award = awards.get(index);
            ItemStack item = award.getItem();
            MyItem guiItem = new MyItem(item);
            if(award.getDisplayName()!=null){
                guiItem.setDisplayName(award.getDisplayName());
            }
            int weight = award.getWeight();
            int sum = lottery.getWeightSum();
            String str = weight==0? "0.00%":df.format((double) weight/sum);
            inv.setItem(i, guiItem
                    .addLore(ChatColor.GOLD +LangUtils.Weight+"/"+LangUtils.WeightSum+ChatColor.AQUA+":"+ChatColor.GREEN+weight +"/"+sum)
                    .addLore(ChatColor.GOLD +LangUtils.Probability+ChatColor.GREEN+str)
                    .addLore(LangUtils.LotteryPool1)
                    .addLore(LangUtils.LotteryPool2)
                    .addLore(LangUtils.LotteryPool3)
                    .getItem());
        }
        return inv;
    }

    @Override
    public Inventory refresh(){
        return new LotteryPoolGui(awards,lottery).getPage(page);
    }

    public void handleClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        //点击奖池物品
        if(e.getRawSlot()>=0&&e.getRawSlot()<=44&&e.getCurrentItem()!=null){

            //被点击的物品

            int index = e.getRawSlot()+45*(page-1);
            Award award = awards.get(index);
            ItemStack itemStack = award.getItem();

            //详细修改
            if(e.isShiftClick()&&e.isLeftClick()&&itemStack!=null){
                player.openInventory(new AwardSetting(awards, index,this).getInventory());
            }

            //删除物品
            if(e.isShiftClick()&&e.isRightClick()&&itemStack!=null){
                awards.remove(index);
                player.openInventory(refresh());
                SerializeUtils.saveLotteryData();
            }
            //修改权重
            if(!e.isShiftClick()&&e.isLeftClick()&&itemStack!=null){
                ReceiveUtils.receiveWeight(player,awards.get(index),this);
            }
        }
        //退出/添加物品
        if(e.isLeftClick()&&e.getRawSlot()==49){
            if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                // 玩家拿起了物品，执行相关操作
                ItemStack cursorItem = e.getCursor();
                ItemStack item = cursorItem.clone();
                // 把玩家拿起的物品放入背包
                player.setItemOnCursor(null);
                player.getInventory().addItem(cursorItem);

                Award award = new Award(item);
                awards.add(award);
                player.openInventory(refresh());
                SerializeUtils.saveLotteryData();

            }
            else {
                // 玩家没有拿起物品，处理点击铁砧返回
                player.openInventory(new LotterySetting(lottery.getName()).getInventory());
            }
        }

        if(e.getRawSlot()==45){
            e.getWhoClicked().openInventory(getPage(this.page-1));
        }else if(e.getRawSlot()==53){
            e.getWhoClicked().openInventory(getPage(this.page+1));
        }else if(e.getRawSlot()==47){
            awards.sort((o1, o2) -> o1.getWeight()-o2.getWeight());
            player.openInventory(getPage(page));
        }else if(e.getRawSlot()==51){
            awards.sort((o1, o2) -> o2.getWeight()-o1.getWeight());
            player.openInventory(getPage(page));
        }
    }

}
