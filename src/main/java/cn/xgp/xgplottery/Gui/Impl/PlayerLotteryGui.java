package cn.xgp.xgplottery.Gui.Impl;

import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolShow;
import cn.xgp.xgplottery.Gui.LotteryGui;
import cn.xgp.xgplottery.Gui.PlayerGui;
import cn.xgp.xgplottery.Lottery.Lottery;
import cn.xgp.xgplottery.Lottery.MyItem;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.NMSUtils;
import cn.xgp.xgplottery.Utils.TimesUtils;
import cn.xgp.xgplottery.Utils.VersionAdapterUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class PlayerLotteryGui extends PlayerGui {
    final Inventory inv = Bukkit.createInventory(this,6*9, ChatColor.GOLD+"做出选择吧！");
    Player player;
    Lottery lottery;
    boolean keyOrTicket;
    boolean single;
    boolean multiple;

    public PlayerLotteryGui(Player player, Lottery lottery, boolean keyOrTicket) {
        this.player = player;
        this.lottery = lottery;
        this.keyOrTicket = keyOrTicket;
    }

    @Override
    public @NotNull Inventory getInventory() {
        loadGui();
        return inv;
    }

    @Override
    public LotteryGui loadGui() {
        ItemStack item = VersionAdapterUtils.getItemInMainHand(player);
        int amount = 0;
        if (item!=null&&item.getItemMeta()!=null){
            if (NMSUtils.checkTag(item, keyOrTicket, lottery.getName())) {
                amount = item.getAmount();
            }
        }
        setBorder(inv);
        String lore = "§7[§cx§7] §b您现在无法抽奖:§4抽奖所需物品不足";


        int emptySlots = VersionAdapterUtils.getPlayerEmptySlot(player);
        //判断个数
        if(amount>0){
            lore = "§7[§a √ §7] §b点击进行一次抽奖！";
            single = true;
            //判断背包是否为满
            if(lottery.isCheckFull()){
                if(emptySlots==0){
                    single=false;
                    lore = "§7[§c x §7] §b您现在无法抽奖:§4背包空间不足";
                }
            }
            if(lottery.getLimitedTimes()>0){
                if(TimesUtils.getTimes(player.getUniqueId(),lottery.getName())>=lottery.getLimitedTimes()){
                    lore = "§7[§c x §7] §b您现在无法抽奖:§4抽奖次数已达上限";
                    single=false;
                }
            }
        }

        inv.setItem(20,new MyItem(Material.CHEST).setDisplayName(ChatColor.GOLD+"单抽出奇迹！").addLore(lore)
                .addEnchant().getItem());

        lore = "§7[§c x §7] §b您现在无法抽奖:§4抽奖所需物品不足";

        //判断个数
        if(amount>=10){
            lore = "§7[§a √ §7] §b点击进行一次十连抽！";
            multiple = true;
            //判断背包是否为满
            if(lottery.isCheckFull()){
                if(emptySlots<=9){
                    multiple=false;
                    lore = "§7[§c x §7] §b您现在无法抽奖:§4背包空间不足";
                }
            }
            if(lottery.getLimitedTimes()>0){
                if(TimesUtils.getTimes(player.getUniqueId(),lottery.getName())+10>lottery.getLimitedTimes()){
                    lore = "§7[§c x §7] §b您现在无法抽奖:§4抽奖次数剩余不足";
                    multiple=false;
                }
            }
        }

        inv.setItem(24,new MyItem(Material.CHEST).setDisplayName(ChatColor.GOLD+"豪爽十连抽！").addLore(lore)
                .addEnchant().getItem());
        String str;
        if(lottery.getMaxTime()>0){
            int times = 0;
            if(TimesUtils.getCurrentLotteryTimes(player.getUniqueId(),lottery.getName())!=null)
                times = TimesUtils.getCurrentLotteryTimes(player.getUniqueId(),lottery.getName()).getTimes();
            str = ChatColor.BLUE+ LangUtils.BoxInformation4 +ChatColor.AQUA+lottery.getMaxTime()+ChatColor.BLUE+ LangUtils.BoxInformation5 + ChatColor.AQUA+ times;
        }else
            str =  ChatColor.BLUE+ LangUtils.BoxInformation6;
        inv.setItem(22,new MyItem(Material.DIAMOND).setDisplayName(ChatColor.GOLD+"提示信息")
                .addLore("§7[§e ! §7] "+ChatColor.BLUE+"你需要手持抽奖物品")
                .addLore(ChatColor.BLUE+"手中数量:"+ChatColor.AQUA+amount)
                .addLore(str)
                .getItem());


        inv.setItem(29,new MyItem(Material.CHEST).setDisplayName(ChatColor.GOLD+"查看普通物品池")
                .setLore("§7[§e ? §7] §b左键点击！ §a或者你其实不在意这些？").getItem());
        inv.setItem(33,new MyItem(Material.CHEST).setDisplayName(ChatColor.GOLD+"查看保底物品池")
                .setLore("§7[§e ? §7] §b左键点击！ §a或者你其实不在意这些？").getItem());
        inv.setItem(31,new MyItem(Material.PAPER).setDisplayName(ChatColor.GOLD+"查看抽奖记录")
                .setLore("§7[§e ? §7] §b左键点击！ §a或者你其实不在意这些？").getItem());
        return this;
    }

    int eggs = 0;
    @Override
    public void handleClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        switch (e.getRawSlot()){
            //上一层
            case 0:
            //退出
            case 8: player.getOpenInventory().close();break;
            case 29: player.openInventory(new LotteryPoolShow(lottery,lottery.getAwards(),this).getInventory()); break;
            case 33: player.openInventory(new LotteryPoolShow(lottery,lottery.getSpAwards(),this).getInventory()); break;
            case 20: {
                loadGui();
                if(!single){
                    return;
                }
                lottery.open(player,false,false);
                break;
            }
            case 24:{
                loadGui();
                if (!multiple){
                    return;
                }
                lottery.open(player,false,true);
                break;
            }
            case 22:{
                eggs++;
                if(eggs==10){
                    inv.setItem(22,new MyItem(command).setDisplayName(ChatColor.GOLD+"小钢炮最可爱.jpg").setLore(ChatColor.BLUE+"This is an Easter egg!").getItem());
                    eggs=0;
                }
                break;
            }
            case 31:{
                player.openInventory(new RecordGui(player,lottery.getName(),this).getInventory());
                break;
            }
            default:
        }
    }
}
