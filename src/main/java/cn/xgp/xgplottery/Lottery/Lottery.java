package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Gui.Impl.Manage.LotteryManageGui;
import cn.xgp.xgplottery.Gui.Impl.Pool.LotteryPoolGui;
import cn.xgp.xgplottery.Gui.Impl.Pool.SpecialPoolGui;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.Impl.BoxAnimation;
import cn.xgp.xgplottery.Lottery.LotteryAnimation.LotteryAnimation;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.Impl.Custom;
import cn.xgp.xgplottery.Lottery.ProbabilityCalculator.ProbabilityCalculator;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import cn.xgp.xgplottery.XgpLottery;
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
public class Lottery {

    private String name = "默认名称";           //奖池名称
    private String animation;                 //抽奖显示动画
    private int maxTime;                    //保底次数
    private List<ItemStack> items;                       //储存奖池物品
    private List<Integer> weights;
    private List<ItemStack> spItems;
    private List<Integer> spWeights;
    private double value;
    private boolean isPoint;


    public Lottery(@NotNull String animation, List<ItemStack> items,List<ItemStack> spItems,boolean isPoint,double value) {
        this(animation,items,-1,spItems,isPoint,value);
    }

    public Lottery(@NotNull String animation, List<ItemStack> items,int maxTime,List<ItemStack> spItems,boolean isPoint,double value) {
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


    @NotNull
    public Map<String, Object> serialize() {
        Map<String, Object> map =new HashMap<>();
        map.put("animation",animation);
        map.put("pool",items);
        map.put("weights",weights);
        map.put("spWeights",spWeights);
        map.put("maxTime",maxTime);
        map.put("spItem",spItems);
        map.put("isPoint",isPoint);
        map.put("value",value);
        return map;
    }

    public static Lottery deserialize(Map<String, Object> map) {
        List<ItemStack> items = new ArrayList<>();
        List<ItemStack> spItems = new ArrayList<>();
        if(map.get("pool")!=null)
            items = (ArrayList<ItemStack>) map.get("pool");
        if(map.get("spItem")!=null)
            spItems = (ArrayList<ItemStack>) map.get("spItem");
        Lottery lottery = new Lottery((String)map.get("animation"), items, (int) map.get("maxTime"), spItems,(boolean)map.get("isPoint"),(double)map.get("value"));
        lottery.setWeights((ArrayList<Integer>) map.get("weights"));
        lottery.setSpWeights((ArrayList<Integer>) map.get("spWeights"));
        return lottery;
    }

    public LotteryAnimation getAnimation(Player player,Lottery lottery,boolean isCmd) {
        switch (animation){
            case "BoxAnimation":
            default: return new BoxAnimation(player,lottery,isCmd);
        }
    }

    public void open(Player player,boolean isCmd){
        LotteryTimes.addTimes(player,getName());
        getAnimation(player,this,isCmd).playAnimation();
    }



    public ProbabilityCalculator getCalculator() {
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

    public int getItemWeight(ItemStack item){
        int index = items.indexOf(item);
        return weights.get(index);
    }

    public int getSpItemWeight(ItemStack item){
        int index = spItems.indexOf(item);
        return spWeights.get(index);
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


    public Lottery setAnimation(String  animation){
        this.animation = animation;
        return this;
    }


    public static void createLottery(Player player){
        player.closeInventory();
        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance,()->{
            try{
                player.sendMessage(ChatColor.RED+ "[XgpLottery]"+ChatColor.GREEN +"开始创建奖池，请输入奖池名称，输入‘cancel’取消：");
                player.sendMessage(ChatColor.RED+"请不要用颜色代码，这是文件名，只在管理页面显示");
                try{
                    String name = XgpLottery.getInput(player).get(15, TimeUnit.SECONDS);
                    name = name.trim();
                    if(XgpLottery.lotteryList.containsKey(name)){
                        player.sendMessage(ChatColor.RED+"这个奖池已经存在了！");
                    }else if(!name.isEmpty()){
                        XgpLottery.lotteryList.put(name,getDefaultLottery(name));
                        player.sendMessage(ChatColor.YELLOW+"创建了名称是："+name+"的奖池，请打开管理页面编辑");
                        SerializeUtils.saveLotteryData();
                    }else{
                        player.sendMessage(ChatColor.RED+"我怎么啥也没收到捏~");
                    }
                }catch (TimeoutException e){
                    player.sendMessage("给你输入的时间已经过了，已取消");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    public static void receiveWeight(Player player,Lottery lottery,ItemStack item,int index,boolean isSpecial){
        List<ItemStack> items;
        if(isSpecial){
            items = lottery.getSpItems();
        }else {
            items = lottery.getItems();
        }
        player.closeInventory();
        Bukkit.getScheduler().runTaskAsynchronously(XgpLottery.instance,()->{
            try{
                player.sendMessage(ChatColor.GOLD+ "[XgpLottery]"+ChatColor.GREEN +"请输入新的权重,可以为0。输入‘cancel’取消：");
                try{
                    String weight  = XgpLottery.getInput(player).get(15, TimeUnit.SECONDS);
                    if(weight!=null&&Integer.parseInt(weight)>=0){ ;
                        if(isSpecial)
                            lottery.changeSpWeight(index,Integer.parseInt(weight));
                        else
                            lottery.changeWeight(index,Integer.parseInt(weight));
                        player.sendMessage(ChatColor.GREEN+"成功将权重修改为"+weight+"!");
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
                        player.sendMessage(ChatColor.RED+"你好像输错了哦~");
                    }
                }catch (TimeoutException e){
                    player.sendMessage(ChatColor.RED+"给你输入的时间已经过了，已取消");
                }catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+"错误的格式/已取消");
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
                player.sendMessage(ChatColor.GOLD+ "[XgpLottery]"+ChatColor.GREEN +"请输入新的保底次数,输入-1为取消保底次数。输入‘cancel’取消");
                try{
                    String times  = XgpLottery.getInput(player).get(15, TimeUnit.SECONDS);
                    if(times!=null){
                        lottery.setMaxTime(Integer.parseInt(times));
                        player.sendMessage(ChatColor.GREEN+"成功将保底次数修改为"+times+"!");
                        new BukkitRunnable(){
                            @Override
                            public void run(){
                                Inventory inventory = new LotteryManageGui().getInventory();
                                Bukkit.getScheduler().runTask(XgpLottery.instance,()->player.openInventory(inventory));
                            }
                        }.runTaskAsynchronously(XgpLottery.instance);
                        SerializeUtils.saveLotteryData();
                    }else{
                        player.sendMessage(ChatColor.RED+"你好像输错了哦~");
                    }
                }catch (TimeoutException e){
                    player.sendMessage(ChatColor.RED+"给你输入的时间已经过了，已取消");
                }catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+"错误的格式/已取消");
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
                player.sendMessage(ChatColor.GOLD+ "[XgpLottery]"+ChatColor.GREEN +"请输入新的价格,输入0为取消售卖。输入‘cancel’取消");
                try{
                    String times  = XgpLottery.getInput(player).get(15, TimeUnit.SECONDS);
                    if(times!=null){
                        lottery.setMaxTime(Integer.parseInt(times));
                        player.sendMessage(ChatColor.GREEN+"成功将保底次数修改为"+times+"!");
                        new BukkitRunnable(){
                            @Override
                            public void run(){
                                Inventory inventory = new LotteryManageGui().getInventory();
                                Bukkit.getScheduler().runTask(XgpLottery.instance,()->player.openInventory(inventory));
                            }
                        }.runTaskAsynchronously(XgpLottery.instance);
                        SerializeUtils.saveLotteryData();
                    }else{
                        player.sendMessage(ChatColor.RED+"你好像输错了哦~");
                    }
                }catch (TimeoutException e){
                    player.sendMessage(ChatColor.RED+"给你输入的时间已经过了，已取消");
                }catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+"错误的格式/已取消");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}

