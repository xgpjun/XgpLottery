package cn.xgp.xgplottery.Gui.Impl.Manage;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotterySettings.LoreSetting;
import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolGui;
import cn.xgp.xgplottery.Gui.Impl.Select.SelectAnimation;
import cn.xgp.xgplottery.Gui.Impl.Select.SelectMultipleAnimation;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.ReceiveUtils;
import cn.xgp.xgplottery.XgpLottery;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class LotterySetting extends LotteryGui {
    private final Inventory inv = Bukkit.createInventory(this,6*9, ChatColor.GOLD+ LangUtils.LotterySettingTitle);
    private final String lotteryName;

    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        setBorder(inv);
        //显示奖池信息
        inv.setItem(4,new MyItem(Material.CHEST)
                .setDisplayName(ChatColor.GOLD+ LangUtils.ManageButton1)
                .setLore(ChatColor.BLUE+LangUtils.BoxInformation3+ChatColor.AQUA+getLottery().getName())
                .getItem());

        //修改普通池物品管理界面
        inv.setItem(10,new MyItem(Material.CHEST)
                .setDisplayName(ChatColor.GOLD+"普通奖池物品管理")
                .addLore(ChatColor.BLUE+"进入管理界面: "+ChatColor.AQUA+"左键")
                .getItem());
        //修改保底池物品管理界面
        inv.setItem(11,new MyItem(Material.CHEST)
                .setDisplayName(ChatColor.GOLD+"保底奖池物品管理")
                .addLore(ChatColor.BLUE+"进入管理界面: "+ChatColor.AQUA+"左键")
                .addEnchant()
                .getItem());


        //设置保底次数
        int maxTime = getLottery().getMaxTime();
        String mt = maxTime>0? String.valueOf(maxTime) :LangUtils.NotSetMaxTime;
        inv.setItem(12,new MyItem(Material.DIAMOND)
                .setDisplayName(ChatColor.GOLD+"设置奖池保底次数")
                .setLore(ChatColor.BLUE+ LangUtils.PoolButton2+ChatColor.RESET+ChatColor.AQUA +mt)
                .addLore(ChatColor.GREEN+"玩家当此抽奖次数达到此值，会从保底物品中获得一件物品。")
                .addLore(ChatColor.BLUE+"更改数值: "+ChatColor.AQUA+"左键")
                .addLore(ChatColor.GOLD+"设置为0则没有保底机制")
                .getItem());
        //设置售卖方式points/money/经验

        inv.setItem(13,new MyItem(Material.GOLD_INGOT)
                .setDisplayName(ChatColor.GOLD+"切换售卖方式")
                .setLore(ChatColor.GREEN+"可选: "+ChatColor.AQUA+ "点券/金币/经验(级)")
                .addLore(ChatColor.BLUE+LangUtils.PoolButton3+ChatColor.AQUA+getLottery().getSellType().getSellType())
                .addLore(ChatColor.BLUE+"切换: "+ChatColor.AQUA+"左键")
                .getItem());
        //设置默认钥匙格式
        inv.setItem(14,new MyItem(getLottery().getKeyItemStack())
                .setDisplayName(ChatColor.GOLD+"设置奖池默认钥匙材质")
                .addLore(ChatColor.BLUE+"切换: "+ChatColor.AQUA+"把物品拖动到此处点击")
                .addEnchant()
                .getItem());
        //设置钥匙名称
        inv.setItem(15,new MyItem(getLottery().getKeyItemStack())
                .setDisplayName(ChatColor.GOLD+"设置奖池默认钥匙名称")
                .addLore(ChatColor.BLUE+"切换: "+ChatColor.AQUA+"左键")
                .addLore(ChatColor.BLUE+"当前名称: "+ChatColor.RESET+getLottery().getKeyName())
                .getItem());
        //设置钥匙lore
        inv.setItem(16,new MyItem(getLottery().getKeyItemStack())
                .setDisplayName(ChatColor.GOLD+"设置奖池默认钥匙lore")
                .addLore(ChatColor.BLUE+"进入设置界面: "+ChatColor.AQUA+"左键")
                .getItem());


        //设置抽奖上限次数
        inv.setItem(19,new MyItem(Material.REDSTONE)
                .setDisplayName(ChatColor.GOLD+"设置奖池抽奖次数上限")
                .addLore(ChatColor.BLUE+"设置: "+ChatColor.AQUA+"左键")
                .addLore(ChatColor.BLUE+"当前: "+ChatColor.AQUA+getLottery().getLimitedTimes())
                .addLore(ChatColor.GREEN+"即达到此次数后玩家无法继续抽奖。")
                .addLore(ChatColor.GREEN+"0为不做限制")
                .getItem());
        //修改单抽动画
        inv.setItem(20,new MyItem(Material.ITEM_FRAME)
                .setDisplayName(ChatColor.GOLD+"设置奖池单抽动画")
                .setLore(ChatColor.BLUE+"当前抽奖动画: "+ChatColor.AQUA+getLottery().getAnimationObject().toLore())
                .addLore(ChatColor.BLUE+"进入设置界面: "+ChatColor.AQUA+"左键")
                .getItem());
        //修改十连抽动画
        inv.setItem(21,new MyItem(Material.ITEM_FRAME)
                .setDisplayName(ChatColor.GOLD+"设置奖池十连抽动画")
                .setLore(ChatColor.BLUE+"当前抽奖动画: "+ChatColor.AQUA+getLottery().getAnimationObject().toLore())
                .addLore(ChatColor.BLUE+"进入设置界面: "+ChatColor.AQUA+"左键")
                .addEnchant()
                .getItem());
        //设置价格
        inv.setItem(22,new MyItem(Material.GOLD_INGOT)
                .setDisplayName(ChatColor.GOLD+"设置价格")
                .setLore(ChatColor.BLUE+"当前价格/经验(级):"+ChatColor.AQUA+getLottery().getValue())
                .addLore(ChatColor.BLUE+"设置: "+ChatColor.AQUA+"左键")
                .getItem());

        //设置默认奖券格式
        inv.setItem(23,new MyItem(getLottery().getTicketItemStack())
                .setDisplayName(ChatColor.GOLD+"设置奖池默认奖券材质")
                .addLore(ChatColor.BLUE+"设置: "+ChatColor.AQUA+"把物品拖动到此处点击")
                .addEnchant()
                .getItem());
        //设置奖券名称
        inv.setItem(24,new MyItem(getLottery().getTicketItemStack())
                .setDisplayName(ChatColor.GOLD+"设置奖池默认奖券名称")
                .addLore(ChatColor.BLUE+"当前名称: "+getLottery().getTicketName())
                .addLore(ChatColor.BLUE+"设置: "+ChatColor.AQUA+"左键")
                .getItem());
        //设置奖券lore
        inv.setItem(25,new MyItem(getLottery().getTicketItemStack())
                .setDisplayName(ChatColor.GOLD+"设置奖池默认奖券lore")
                .addLore(ChatColor.BLUE+"进入设置界面: "+ChatColor.AQUA+"左键")
                .getItem());


        inv.setItem(28,new MyItem(Material.ANVIL)
                .setDisplayName(ChatColor.GOLD+"设置背包满时是否禁止抽奖")
                .setLore(ChatColor.BLUE+"当前状况:"+ChatColor.AQUA+(getLottery().isCheckFull()?"是":"否"))
                .addLore(ChatColor.BLUE+"切换: "+ChatColor.AQUA+"左键")
                .getItem());

        return this;
    }

    public Lottery getLottery(){
        return XgpLottery.lotteryList.get(lotteryName);
    }
    @Override
    public void handleClick(InventoryClickEvent e) {
        int slot = e.getRawSlot();
        Player player = (Player) e.getWhoClicked();
        switch (slot){
            case 0: player.openInventory(new LotteryManageGui().getInventory());break;
            case 8: player.getOpenInventory().close();break;
            case 10: player.openInventory(new LotteryPoolGui(getLottery().getAwards(),getLottery()).getInventory());break;
            case 11: player.openInventory(new LotteryPoolGui(getLottery().getSpAwards(),getLottery()).getInventory());break;
            case 12: ReceiveUtils.setMaxTime(player,getLottery());break;
            case 13:{
                getLottery().changeSellType();
                player.openInventory(this.getInventory());
                break;
            }
            case 14:{
                // 玩家拿起了物品，执行相关操作
                ItemStack cursorItem = e.getCursor();
                if(cursorItem==null)
                    return;
                ItemStack item = cursorItem.clone();
                // 把玩家拿起的物品放入背包
                player.setItemOnCursor(null);
                player.getInventory().addItem(cursorItem);
                item.setAmount(1);
                getLottery().setKeyItemStack(item);
                player.openInventory(this.getInventory());
                break;
            }
            case 15: ReceiveUtils.setKeyName(player,this,true);break;
            case 16: player.openInventory(new LoreSetting(getLottery().getKeyLore(),this).getInventory()); break;
            case 19: ReceiveUtils.setLimitedTime(player,this);break;
            case 20: player.openInventory(new SelectAnimation(getLottery(),this).getInventory());break;
            case 21: player.openInventory(new SelectMultipleAnimation(getLottery(),this).getInventory()); break;//十连抽
            case 22: ReceiveUtils.setValue(player,this);break;
            case 23:{
                // 玩家拿起了物品，执行相关操作
                ItemStack cursorItem = e.getCursor();
                if(cursorItem==null)
                    return;
                ItemStack item = cursorItem.clone();
                // 把玩家拿起的物品放入背包
                player.setItemOnCursor(null);
                player.getInventory().addItem(cursorItem);
                item.setAmount(1);
                getLottery().setTicketItemStack(item);
                player.openInventory(this.getInventory());
                break;
            }
            case 24: ReceiveUtils.setKeyName(player,this,false);break;
            case 25: player.openInventory(new LoreSetting(getLottery().getTicketLore(),this).getInventory()); break;
            case 28: getLottery().setCheckFull(!getLottery().isCheckFull());player.openInventory(this.getInventory());break;
            default: break;
        }
    }
}
