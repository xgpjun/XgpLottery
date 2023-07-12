package cn.xgp.xgplottery.Lottery;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@AllArgsConstructor
@Data
public class Award {
    private ItemStack item;
    private int weight;
    private List<String> commands;
    //是否给予物品
    private boolean giveItem;
    //是否给执行命令
    private boolean executeCommands;
    //是否播报
    private boolean broadCast;
    private String displayName;

    public Award(ItemStack item ,int weight){
        this.item = item;
        this.weight = weight;
        this.commands = new ArrayList<>();
        this.giveItem = true;
        this.executeCommands = false;
        this.broadCast = false;
        this.displayName = null;
    }
    public Award(ItemStack item){
        this(item,1);
    }

    public String[] getCommandsString(){
        int size = commands.size();
        if(size==0)
            return new String[]{"还没有任何指令！"};
        return IntStream.range(0, size)
                .mapToObj(i -> (i + 1) + ": " + commands.get(i))
                .toArray(String[]::new);
    }

    public ItemStack getRecordDisplayItem(){
        MyItem i = new MyItem(item).setDisplayName(displayName);
        if(!commands.isEmpty()){
            i.addLore(ChatColor.BLUE+ "执行命令：");
            for (int x =0;x<commands.size();x++){
                i.addLore((x+1)+": "+commands.get(x));
            }
        }
        return i.getItem();
    }
}
