package cn.xgp.xgplottery.Gui.Impl.Pool;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryManageGui;
import cn.xgp.xgplottery.Gui.Impl.Select.SelectAnimation;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl.Custom;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.text.DecimalFormat;
import java.util.Objects;

//奖池详细内容
public class LotteryPoolGui extends LotteryGui {
    private final Inventory inv ;
    @Getter
    private final Lottery lottery;


    public LotteryPoolGui(Lottery lottery){
        this.lottery = lottery;
        inv = Bukkit.createInventory(this,6*9, ChatColor.YELLOW+ lottery.getName()+ "-奖池内容");
    }
    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        if(lottery.getCalculatorObject() instanceof Custom){
            DecimalFormat df = new DecimalFormat("0.00%");
            int index =0;
            for(int i =0;i<lottery.getItems().size();i++) {
                ItemStack item = lottery.getItems().get(i);
                MyItem guiItem = new MyItem(item);
                int weight = lottery.getWeights().get(i);
                int sum = lottery.getWeightSum();
                inv.setItem(index, guiItem
                        .setLore(ChatColor.GOLD +"权重/总权重："+ChatColor.GREEN+weight +"/"+sum)
                        .addLore(ChatColor.GOLD +"概率："+ChatColor.GREEN+df.format((double) weight/sum))
                        .getItem());
                index++;
            }

            for (index = 45;index<=53;index++){
                inv.setItem(index,borderGlass);
            }

            inv.setItem(49,new MyItem(Material.ANVIL)
                    .setDisplayName(ChatColor.YELLOW+"操作指南")
                    .setLore(ChatColor.RED+"空手左键点击本物品返回列表")
                    .addLore(ChatColor.GOLD+"出售方式："+ChatColor.AQUA+(lottery.isPoint()?"点券":"金币"))
                    .addLore(ChatColor.GOLD+"拖动物品点击加入物品")
                    .addLore(ChatColor.GOLD+"shift+右键点击[奖品]删除物品")
                    .addLore(ChatColor.GOLD+"左键物品点击[奖品]设置权重（越小概率越低）")
                    .addLore(ChatColor.RED+"Shift+右键点击选择抽奖动画")
                    .addLore(ChatColor.RED+"右键点击本物品切换售卖方式")
                    .getItem());
        }

        return this;
    }
    public void handleClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getRawSlot()>=0&&e.getRawSlot()<=44&&e.getCurrentItem()!=null){
            //得到奖池对象
            Lottery lottery = ((LotteryPoolGui) Objects.requireNonNull(e.getInventory().getHolder())).getLottery();
            //被点击的物品
            int index = e.getSlot();
            ItemStack itemStack = lottery.getItems().get(index);
            //删除物品
            if(e.isShiftClick()&&e.isRightClick()&&itemStack!=null){
                if(lottery.getItems().contains(itemStack)){
                    lottery.delItem(e.getRawSlot());
                    player.openInventory(new LotteryPoolGui(lottery).getInventory());
                    SerializeUtils.saveLotteryData();
                }
            }else if(!e.isShiftClick()&&e.isLeftClick()&&itemStack!=null){
                Lottery.receiveWeight(player,lottery,e.getRawSlot(),false);
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
                Lottery lottery = ((LotteryPoolGui) Objects.requireNonNull(e.getInventory().getHolder())).getLottery();
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
        if(!e.isShiftClick()&&e.isRightClick()&&e.getRawSlot()==49){
            lottery.setPoint(!lottery.isPoint());
            player.openInventory(new LotteryPoolGui(lottery).getInventory());
            SerializeUtils.saveLotteryData();
        }
        if(e.isShiftClick()&&e.isRightClick()&&e.getRawSlot()==49){
            player.openInventory(new SelectAnimation(lottery).getInventory());
        }
    }

}
