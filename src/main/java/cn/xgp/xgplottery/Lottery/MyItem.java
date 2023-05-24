package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Utils.nmsUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MyItem {
    private ItemStack item;
    private final ItemMeta itemMeta;

    public MyItem(Material material,int amount,byte damage){
        item = new ItemStack(material,amount,damage);
        itemMeta = item.getItemMeta();
    }

    public MyItem(Material material){
        item = new ItemStack(material);
        itemMeta = item.getItemMeta();
    }
    public MyItem(ItemStack item){
        this.item = item.clone();
        itemMeta = item.getItemMeta();
    }

    public MyItem setDisplayName(String DisplayName){
        itemMeta.setDisplayName(DisplayName);
        return this;
    }

    public MyItem setLore(String... lore){
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public MyItem insertLore(int index, String... lore){
        List<String> loreList = getLoreList();

        if(loreList.isEmpty()){
            itemMeta.setLore(Arrays.asList(lore));
        }else {
            if(index<0){
                index = loreList.size()+index;
            }
            loreList.addAll(index, Arrays.asList(lore));
            itemMeta.setLore(loreList);
        }
        return this;
    }

    private List<String> getLoreList() {
        List<String> rawLore = itemMeta.getLore();
        return (rawLore != null) ? new ArrayList<>(rawLore) : new ArrayList<>();
    }

    public MyItem addLore(String... lore){
        return insertLore(-1,lore);
    }

    public MyItem setAmount(int amount){
        item.setAmount(amount);
        return this;
    }


    public ItemStack getItem() {
        item.setItemMeta(itemMeta);
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }


    public MyItem addEnchant(){
        itemMeta.addEnchant(Enchantment.ARROW_INFINITE,19,true);
        if(nmsUtils.versionToInt>7)
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }
}
