package cn.xgp.xgplottery.Gui;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MyItem {
    private ItemStack item;
    private ItemMeta itemMeta;

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
        if(lore==null)
            return this;
        List<String> rawLore = itemMeta.getLore();

        if(rawLore == null||rawLore.size()==0){
            itemMeta.setLore(Arrays.asList(lore));
        }
        if(index<0){
            index = rawLore.size()+index;
        }
        if(rawLore instanceof ArrayList || rawLore instanceof Vector || rawLore instanceof LinkedList){
            rawLore.addAll(index, Arrays.asList(lore));
            itemMeta.setLore(rawLore);
            return this;
        }
        List<String> properLore = Arrays.asList(rawLore.toArray(new String[0]));
        properLore.addAll(index, Arrays.asList(lore));
        itemMeta.setLore(properLore);
        return this;
    }

    public MyItem addLore(String... lore){
        return insertLore(-1,lore);
    }

    public MyItem setAmount(int amount){
        item.setAmount(amount);
        return this;
    }

    public MyItem setEnchants(int level, Enchantment enchantment){
        itemMeta.addEnchant(enchantment,level,true);
        return this;
    }
    public MyItem removeEnchants(Enchantment enchantment){
        itemMeta.removeEnchant(enchantment);
        return this;
    }
    public MyItem setAttributeModifier(Attribute attribute, AttributeModifier attributeModifier){
        itemMeta.addAttributeModifier(attribute,attributeModifier);
        return this;
    }

    public ItemStack getItem() {
        item.setItemMeta(itemMeta);
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public void setItemMeta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
    }

    public MyItem addEnchant(){
        itemMeta.addEnchant(Enchantment.ARROW_INFINITE,19,true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }
}
