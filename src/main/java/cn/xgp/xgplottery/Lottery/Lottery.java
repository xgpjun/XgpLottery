package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryManageGui;
import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolGui;
import cn.xgp.xgplottery.Gui.Impl.Pool.SpecialPoolGui;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.BoxAnimation;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.SelectItemAnimation;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl.Custom;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import cn.xgp.xgplottery.Utils.LangUtils;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.Utils.TimesUtils;
import cn.xgp.xgplottery.XgpLottery;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Data
@AllArgsConstructor
public class Lottery {

    private String name;
    private String animation;                 //抽奖显示动画
    private int maxTime;                    //保底次数
    private List<ItemStack> items;                       //储存奖池物品
    private List<Integer> weights;
    private List<ItemStack> spItems;
    private List<Integer> spWeights;
    private int value;
    private boolean isPoint;



    public Lottery(@NotNull String animation, List<ItemStack> items,List<ItemStack> spItems,boolean isPoint,int value) {
        this(animation,items,-1,spItems,isPoint,value);
    }

    public Lottery(@NotNull String animation, List<ItemStack> items,int maxTime,List<ItemStack> spItems,boolean isPoint,int value) {
        this.animation = animation;
        this.items = items;
        this.maxTime = maxTime;
        this.spItems = spItems;
        this.isPoint = isPoint;
        this.value = value;
    }

    public static Lottery getDefaultLottery(String name){
        Lottery lottery = new Lottery("default", new ArrayList<>(),-1,new ArrayList<>(),true,0);
        lottery.setName(name);
        return lottery;
    }

    public LotteryAnimation getAnimationObject(Player player, Lottery lottery, boolean isCmd) {
        switch (animation){
            case "SelectItemAnimation": return new SelectItemAnimation(player,lottery,isCmd);
            case "BoxAnimation":
            default: return new BoxAnimation(player,lottery,isCmd);
        }
    }

    public void open(Player player,boolean isCmd){
        //抽奖
        if(getCommonWeightSum()<=0){
            player.sendMessage(ChatColor.RED+LangUtils.PoolIsEmpty);
            return;
        }

        TimesUtils.addTimes(player,getName());
        getAnimationObject(player,this,isCmd).playAnimation();
    }



    public ProbabilityCalculator getCalculatorObject() {
        return new Custom();
    }

    public void addItem(ItemStack item){
        if(items ==null)
            items = new ArrayList<>();
        items.add(item);
        if(weights == null)
            weights = new ArrayList<>();
        weights.add(1);
    }
    public void addSpItem(ItemStack item){
        if(spItems==null)
            spItems = new ArrayList<>();
        spItems.add(item);
        if(spWeights == null)
            spWeights = new ArrayList<>();
        spWeights.add(1);
    }

    public void delItem(int index){
            items.remove(index);
            weights.remove(index);
    }

    public void delSpItem(int index){
        spItems.remove(index);
        spWeights.remove(index);
    }


    public int getAmount(){
        return items.size();
    }

    public int getSpAmount(){
        return spItems.size();
    }

    public void changeWeight(int index,int weight){
        this.weights.remove(index);
        this.weights.add(index,weight);
    }

    public void changeSpWeight(int index,int weight){
        this.weights.remove(index);
        this.weights.add(index,weight);
    }


    public int getWeightSum(){
        return getCommonWeightSum()+getSpWeightSum();
    }

    public int getSpWeightSum(){
        int addition=0;
        if(spWeights!=null)
            for(Integer weight : spWeights)
                addition += weight;
        return addition;
    }
    public int getCommonWeightSum(){
        int addition=0;
        if(weights!=null)
            for (Integer weight : weights)
                addition += weight;
        return addition;
    }

    public List<Integer> getSpWeights() {
        if(spWeights==null)
            spWeights = new ArrayList<>();
        return spWeights;
    }

    public List<Integer> getWeights() {
        if(weights==null)
            weights = new ArrayList<>();
        return weights;
    }


    public static void createLottery(Player player){
        player.closeInventory();
        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance,()->{
            try{
                player.sendMessage(ChatColor.RED+ "[XgpLottery]"+ChatColor.GREEN +LangUtils.CreateLottery);
                player.sendMessage(ChatColor.RED+LangUtils.DontUseColor);
                try{
                    String name = XgpLottery.getInput(player).get(15, TimeUnit.SECONDS);
                    name = name.trim();
                    if(name.equals("cancel")){
                        player.sendMessage(ChatColor.RED+LangUtils.WrongType);
                        return;
                    }
                    if(XgpLottery.lotteryList.containsKey(name)){
                        player.sendMessage(ChatColor.RED+LangUtils.LotteryAlreadyExists);
                    }else if(!name.isEmpty()){
                        XgpLottery.lotteryList.put(name,getDefaultLottery(name));
                        player.sendMessage(ChatColor.YELLOW+LangUtils.CreateLotterySuccessfully.replace("%name%",name));
                        SerializeUtils.saveLotteryData();
                        Bukkit.getScheduler().runTask(XgpLottery.instance,()-> player.openInventory(new LotteryManageGui().getInventory()));
                    }else{
                        player.sendMessage(ChatColor.RED+LangUtils.WrongType);
                    }
                }catch (TimeoutException e){
                    player.sendMessage(ChatColor.RED+ LangUtils.TimeOut);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    public static void receiveWeight(Player player,Lottery lottery,int index,boolean isSpecial){
        player.closeInventory();
        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance,()->{
            try{
                player.sendMessage(ChatColor.GOLD+ "[XgpLottery]"+ChatColor.GREEN + LangUtils.ReceiveWeight);
                try{
                    String weight  = XgpLottery.getInput(player).get(15, TimeUnit.SECONDS);
                    if(weight!=null&&Integer.parseInt(weight)>=0){
                        if(isSpecial)
                            lottery.changeSpWeight(index,Integer.parseInt(weight));
                        else
                            lottery.changeWeight(index,Integer.parseInt(weight));
                        player.sendMessage(ChatColor.GREEN+LangUtils.ChangeWeightSuccessfully+weight+"!");
                        new BukkitRunnable(){
                            @Override
                            public void run(){
                                Inventory inventory;
                                if(isSpecial)
                                    inventory = new SpecialPoolGui(lottery).getInventory();
                                else
                                    inventory = new LotteryPoolGui(lottery).getInventory();
                                Bukkit.getScheduler().runTask(XgpLottery.instance,()->player.openInventory(inventory));
                            }
                        }.runTaskAsynchronously(XgpLottery.instance);
                        SerializeUtils.saveLotteryData();
                    }else{
                        player.sendMessage(ChatColor.RED+LangUtils.WrongType);
                    }
                }catch (TimeoutException e){
                    player.sendMessage(ChatColor.RED+LangUtils.TimeOut);
                }catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+LangUtils.WrongType);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }


    public static void setMaxTime(Player player,Lottery lottery){
        player.closeInventory();
        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance,()->{
            try{
                player.sendMessage(ChatColor.GOLD+ "[XgpLottery]"+ChatColor.GREEN +LangUtils.SetMaxTime);
                try{
                    String times  = XgpLottery.getInput(player).get(15, TimeUnit.SECONDS);
                    if(times!=null){
                        lottery.setMaxTime(Integer.parseInt(times));
                        player.sendMessage(ChatColor.GREEN+LangUtils.ChangeTimeSuccessfully+times+"!");
                        new BukkitRunnable(){
                            @Override
                            public void run(){
                                Inventory inventory = new LotteryManageGui().getInventory();
                                Bukkit.getScheduler().runTask(XgpLottery.instance,()->player.openInventory(inventory));
                            }
                        }.runTaskAsynchronously(XgpLottery.instance);
                        SerializeUtils.saveLotteryData();
                    }else{
                        player.sendMessage(ChatColor.RED+LangUtils.WrongType);
                    }
                }catch (TimeoutException e){
                    player.sendMessage(ChatColor.RED+LangUtils.TimeOut);
                }catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+LangUtils.WrongType);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public static void setValue(Player player,Lottery lottery){
        player.closeInventory();
        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance,()->{
            try{
                player.sendMessage(ChatColor.GOLD+ "[XgpLottery]"+ChatColor.GREEN +LangUtils.SetValue);
                try{
                    String value  = XgpLottery.getInput(player).get(15, TimeUnit.SECONDS);
                    if(value!=null){
                        lottery.setValue(Integer.parseInt(value));
                        player.sendMessage(ChatColor.GREEN+LangUtils.SetValueSuccessfully+value+"!");
                        new BukkitRunnable(){
                            @Override
                            public void run(){
                                Inventory inventory = new LotteryManageGui().getInventory();
                                Bukkit.getScheduler().runTask(XgpLottery.instance,()->player.openInventory(inventory));
                            }
                        }.runTaskAsynchronously(XgpLottery.instance);
                        SerializeUtils.saveLotteryData();
                    }else{
                        player.sendMessage(ChatColor.RED+LangUtils.WrongType);
                    }
                }catch (TimeoutException e){
                    player.sendMessage(ChatColor.RED+ LangUtils.TimeOut);
                }catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+LangUtils.WrongType);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}

