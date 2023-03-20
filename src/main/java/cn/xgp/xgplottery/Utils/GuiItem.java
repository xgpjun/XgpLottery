package cn.xgp.xgplottery.Utils;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class GuiItem {
    private ItemStack item;
    private ItemMeta itemMeta;

    public GuiItem(Material material){
        item = new ItemStack(material);
        itemMeta = item.getItemMeta();
    }

    public GuiItem setDisplayName(String DisplayName){
        itemMeta.setDisplayName(DisplayName);
        return this;
    }

    public GuiItem setLore(String... lore){
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public GuiItem addLore(String... lore){
        List<String> oldLore = itemMeta.getLore();
        if(oldLore == null||oldLore.size()==0){
            itemMeta.setLore(Arrays.asList(lore));
            return this;
        }
        List<String> newLore = Arrays.asList(oldLore.toArray(new String[0]));
        newLore.addAll(oldLore.size()-1, Arrays.asList(lore));
        itemMeta.setLore(newLore);
        return this;
    }

    public GuiItem setAmount(int amount){
        item.setAmount(amount);
        return this;
    }

    public GuiItem setEnchants(int level, Enchantment enchantment){
        itemMeta.addEnchant(enchantment,level,true);
        return this;
    }
    public GuiItem removeEnchants(Enchantment enchantment){
        itemMeta.removeEnchant(enchantment);
        return this;
    }
    public GuiItem setAttributeModifier(Attribute attribute, AttributeModifier attributeModifier){
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
}
