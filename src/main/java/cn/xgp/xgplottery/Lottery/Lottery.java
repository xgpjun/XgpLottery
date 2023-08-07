package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.*;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.MultipleAnimation;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl.Custom;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.NMSUtils;
import cn.xgp.xgplottery.Utils.TimesUtils;
import cn.xgp.xgplottery.Utils.VersionAdapterUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
public class Lottery {

    private String name;
    private String animation;                 //抽奖显示动画
    private String multipleAnimation;
    private int maxTime;                    //保底次数

    @Deprecated
    private transient List<ItemStack> items;                       //储存奖池物品
    @Deprecated
    private transient List<Integer> weights;
    @Deprecated
    private transient List<ItemStack> spItems;
    @Deprecated
    private transient List<Integer> spWeights;

    private List<Award> awards;
    private List<Award> spAwards;

    private int value;
    @Deprecated
    private transient boolean isPoint;

    private String keyMaterial ;
    private String keyName;
    private List<String> keyLore;
    private String ticketMaterial ;
    private String ticketName;
    private List<String> ticketLore;

    private SellType sellType;
    private int limitedTimes;

    private boolean isCheckFull;
    public Lottery(String name, String animation, String multipleAnimation, int maxTime, List<Award> awards, List<Award> spAwards, int value, SellType sellType, int limitedTimes,boolean isCheckFull) {
        this.name = name;
        this.animation = animation;
        this.multipleAnimation = multipleAnimation;
        this.maxTime = maxTime;
        this.awards = awards;
        this.spAwards = spAwards;
        this.value = value;
        this.sellType = sellType;
        this.limitedTimes = limitedTimes;
        this.isCheckFull = isCheckFull;
    }
    /**
     * 旧版本数据结构反序列化
     *
     */
    @Deprecated
    public Lottery(String name,String animation,int maxTime,List<ItemStack> items,List<Integer> weights,List<ItemStack> spItems,List<Integer> spWeights,int value,boolean isPoint){
        this.name = name;
        this.animation = animation;
        this.maxTime = maxTime;
        this.items = items;
        this.spItems = spItems;
        this.weights = weights;
        this.spWeights = spWeights;
        this.value = value;
        this.isPoint = isPoint;
    }

    public static Lottery getDefaultLottery(String name){
        return new Lottery(name,"BoxAnimation","DefaultMultipleAnimation",-1, new ArrayList<>(), new ArrayList<>(),0,SellType.POINTS,0,false);
    }

    public LotteryAnimation getAnimationObject(Player player, Lottery lottery) {
        switch (animation){
            case "MarqueeAnimation":
                return new MarqueeAnimation(player, lottery);
            case "SelectItemAnimation":
                return new SelectItemAnimation(player, lottery);
            case "ColorfulAnimation":
                return new ColorfulAnimation(player, lottery);
            case "VoidAnimation":
                return new VoidAnimation(player, lottery);
            case "BoxAnimation":
            default:
                return new BoxAnimation(player, lottery);
        }
    }

    public MultipleAnimation getMultipleAnimationObject(Player player, Lottery lottery){
        if(multipleAnimation==null)
            multipleAnimation="DefaultMultipleAnimation";
        switch (multipleAnimation){
            case "MultipleSelectItemAnimation": return new MultipleSelectItemAnimation(player,lottery);
            case "SimpleMultipleAnimation": return new SimpleMultipleAnimation(player,lottery);
            case "BoxMultipleAnimation": return new BoxMultipleAnimation(player,lottery);
            case "DefaultMultipleAnimation":
            default: return new DefaultMultipleAnimation(player,lottery);
        }
    }

    /**
     * 用于获取抽奖动画的描述
     * @return 默认的抽奖动画实例
     */
    public LotteryAnimation getAnimationObject() {
        return getAnimationObject(null,this);
    }

    public void open(Player player,boolean isCmd,boolean isMultiple){
        if(getCommonWeightSum()<=0){
            player.sendMessage(ChatColor.RED+LangUtils.PoolIsEmpty);
            return;
        }
        player.sendMessage(ChatColor.GOLD + LangUtils.LotteryPrefix + ChatColor.GREEN + LangUtils.LotteryInformation.replace("%time%", Integer.toString(TimesUtils.getCurrentTimes(player.getUniqueId(), getName()) + 1)));
        //十连抽
        if(isMultiple){
            if(!isCmd){
                ItemStack item = VersionAdapterUtils.getItemInMainHand(player);
                if (item == null || !NMSUtils.checkTag(item))
                    return;
                if (item.getAmount() < 10) {
                    player.sendMessage(ChatColor.RED + "别想投机取巧！");
                } else if (item.getAmount() == 10) {
                    VersionAdapterUtils.setItemInMainHand(player, null);
                } else {
                    item.setAmount(item.getAmount() - 10);
                }
            }
            getMultipleAnimationObject(player, this).playAnimation();
        }else {
            if(!isCmd) {
                ItemStack item = VersionAdapterUtils.getItemInMainHand(player);
                if (item == null || !NMSUtils.checkTag(item))
                    return;
                if (item.getAmount() == 0) {
                    player.sendMessage(ChatColor.RED + "别想投机取巧！");
                } else if (item.getAmount() == 1) {
                    VersionAdapterUtils.setItemInMainHand(player, null);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }
            }
            getAnimationObject(player, this).playAnimation();
        }
    }



    public ProbabilityCalculator getCalculatorObject() {
        return new Custom();
    }

    @Deprecated
    public void addItem(ItemStack item){
        if(items ==null)
            items = new ArrayList<>();
        items.add(item);
        if(weights == null)
            weights = new ArrayList<>();
        weights.add(1);
    }
    @Deprecated
    public void addSpItem(ItemStack item){
        if(spItems==null)
            spItems = new ArrayList<>();
        spItems.add(item);
        if(spWeights == null)
            spWeights = new ArrayList<>();
        spWeights.add(1);
    }

    @Deprecated
    public void delSpItem(int index){
        spItems.remove(index);
        spWeights.remove(index);
    }


    public int getAmount(){
        return awards.size();
    }

    public int getSpAmount(){
        return spAwards.size();
    }

    public int getWeightSum(){
        return getCommonWeightSum()+getSpWeightSum();
    }

    public int getSpWeightSum(){
        int addition=0;
        if(spAwards!=null)
            for(Award award : spAwards)
                addition += award.getWeight();
        return addition;
    }
    public int getCommonWeightSum(){
        int addition=0;
        if(awards!=null)
            for (Award award : awards)
                addition += award.getWeight();
        return addition;
    }

    @Deprecated
    public List<Integer> getSpWeights() {
        if(spWeights==null)
            spWeights = new ArrayList<>();
        return spWeights;
    }

    @Deprecated
    public List<Integer> getWeights() {
        if(weights==null)
            weights = new ArrayList<>();
        return weights;
    }

    public ItemStack getKeyItemStack(){
        if(keyMaterial==null)
            keyMaterial = NMSUtils.toNBTString(new ItemStack(Material.BONE));
        ItemStack item = NMSUtils.toItem(keyMaterial);
        if(item==null||item.getItemMeta()==null)
            item = new ItemStack(Material.BONE);
        return item;
    }

    public void setKeyItemStack(ItemStack itemStack) {
        this.keyMaterial = NMSUtils.toNBTString(itemStack);
    }

    public String getKeyName(){
        if(keyName==null){
            keyName = ChatColor.GOLD+name+"-"+LangUtils.KeyName;
        }
        return keyName;
    }
    public void setKeyName(String keyName){
        this.keyName = keyName;
    }

    public List<String> getKeyLore() {
        if(keyLore==null){
            keyLore = new ArrayList<>(Collections.singletonList(ChatColor.GOLD + "✦" + ChatColor.AQUA + LangUtils.KeyLore));
        }
        return keyLore;
    }

    //
    public ItemStack getTicketItemStack(){
        if(ticketMaterial==null)
            ticketMaterial = NMSUtils.toNBTString(new ItemStack(Material.PAPER));
        ItemStack item = NMSUtils.toItem(ticketMaterial);
        if(item==null||item.getItemMeta()==null)
            item = new ItemStack(Material.PAPER);
        return item;
    }

    public void setTicketItemStack(ItemStack itemStack) {
        this.ticketMaterial = NMSUtils.toNBTString(itemStack);
    }

    public String getTicketName(){
        if(ticketName==null){
            ticketName = ChatColor.GOLD+name+"-"+LangUtils.TicketName;
        }
        return ticketName;
    }
    public void setTicketName(String ticketName){
        this.ticketName = ticketName;
    }

    public List<String> getTicketLore() {
        if(ticketLore==null){
            ticketLore = new ArrayList<>(Collections.singletonList(ChatColor.GOLD + "✦" + ChatColor.AQUA + LangUtils.TicketLore));
        }
        return ticketLore;
    }
    @Deprecated
    public boolean isKey(ItemStack itemStack){
        return getKeyName().equals(new MyItem(itemStack).getDisplayName())&&getKeyLore().equals(new MyItem(itemStack).getLoreList());
    }

    public SellType getSellType() {
        if(sellType==null){
            if(isPoint){
                sellType=SellType.POINTS;
            }else {
                sellType=SellType.MONEY;
            }
        }
        return sellType;
    }

    public void changeSellType(){
        SellType[] values = SellType.values();
        sellType = values[(sellType.ordinal() + 1) % values.length];
    }

    public ItemStack showFakeItem(){
        int item = getAmount();
        int sp = getSpAmount();
        Random random = new Random();
        int next = random.nextInt(item+sp);
        return next>=item?getSpAwards().get(next-item).getRecordDisplayItem().clone():getAwards().get(next).getRecordDisplayItem().clone();

    }

}

