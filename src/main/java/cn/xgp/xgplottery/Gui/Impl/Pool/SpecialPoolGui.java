package cn.xgp.xgplottery.Gui.Impl.Pool;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryManageGui;
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

public class SpecialPoolGui extends LotteryGui{
    private final Inventory inv ;
    @Getter
    private final Lottery lottery;
    //TODO 文本自定义

    public SpecialPoolGui(Lottery lottery){
        this.lottery = lottery;
        inv = Bukkit.createInventory(this,6*9, ChatColor.YELLOW+ lottery.getName()+ "-保底物品内容");
    }
    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {

        if(lottery.getCalculatorObject() instanceof Custom){
            int index =0;
            DecimalFormat df = new DecimalFormat("0.00%");
            for(int i =0;i<lottery.getSpItems().size();i++) {
                ItemStack item = lottery.getSpItems().get(i);
                MyItem guiItem = new MyItem(item);
                int weight = lottery.getSpWeights().get(i);
                int sum = lottery.getWeightSum();
                inv.setItem(index, guiItem
                        .setLore(ChatColor.GOLD +"权重/总权重："+ChatColor.GREEN+weight +"/"+sum)
                        .addLore(ChatColor.GOLD +"概率："+ChatColor.GREEN+df.format((double) weight/sum))
                        .getItem());
                index++;
            }
            for (index = 45;index<=53;index++) {
                inv.setItem(index, borderGlass);
            }

            inv.setItem(49,new MyItem(Material.ANVIL)
                    .setDisplayName(ChatColor.YELLOW+"操作指南")
                    .setLore(ChatColor.RED+"空手左键点击本物品返回列表")
                    .addLore(ChatColor.RED+"右键点击本物品切换售卖方式")
                    .addLore(ChatColor.GOLD+"出售方式："+ChatColor.AQUA+(lottery.isPoint()?"点券":"金币"))
                    .addLore(ChatColor.GOLD+"拖动物品点击加入物品")
                    .addLore(ChatColor.GOLD+"shift+右键点击删除物品")
                    .addLore(ChatColor.GOLD+"左键点击设置权重（越小概率越低）")
                    .getItem());

        }

        return this;
    }
    public void handleClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getRawSlot()>=0&&e.getRawSlot()<=44&&e.getCurrentItem()!=null){
            //得到奖池对象
            Lottery lottery = ((SpecialPoolGui) Objects.requireNonNull(e.getInventory().getHolder())).getLottery();
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
                Lottery.receiveWeight(player,lottery,e.getRawSlot(),true);
            }
        }
        //退出/添加物品
        if(e.isLeftClick()&&e.getRawSlot()==49){
            if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                // 玩家拿起了物品，执行相关操作
                ItemStack item = e.getCursor().clone();
                // 把玩家拿起的物品放入背包
                player.setItemOnCursor(item);
                Lottery lottery = ((SpecialPoolGui) Objects.requireNonNull(e.getInventory().getHolder())).getLottery();
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
        if(e.isRightClick()&&e.getRawSlot()==49){
            lottery.setPoint(!lottery.isPoint());
            player.openInventory(new LotteryPoolGui(lottery).getInventory());
            SerializeUtils.saveLotteryData();
        }
    }
}
