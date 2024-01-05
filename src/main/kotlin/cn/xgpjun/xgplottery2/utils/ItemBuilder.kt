package cn.xgpjun.xgplottery2.utils

import cn.xgpjun.xgplottery2.hook.PlaceholderAPIHook
import cn.xgpjun.xgplottery2.manager.NMSManager
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*


class MyItemBuilder {
    private var item: ItemStack
    private var itemMeta: ItemMeta

    constructor(material: Material, amount: Int, damage: Byte) {
        item = ItemStack(material, amount, damage.toShort())
        itemMeta = item.itemMeta!!
    }

    constructor(material: Material) {
        item = ItemStack(material)
        if(material == (Material.AIR)){
            item = missingItem
        }
        itemMeta = item.itemMeta!!
    }

    constructor(itemStack: ItemStack?) {
        var item = itemStack
        if (item == null) {
            item = missingItem
        }
        this.item = item.clone()
        itemMeta = item.itemMeta!!
    }

    fun setDisplayName(displayName: String?): MyItemBuilder {
        displayName?. run { itemMeta.setDisplayName(displayName)
        return this@MyItemBuilder} ?:return this
    }

    fun setMaterial(material: Material):MyItemBuilder{
        item = getItem()
        item.type = material
        itemMeta = item.itemMeta!!
        return this
    }

    val displayName: String
        get() = itemMeta.displayName

    fun setLore(vararg lore: String?): MyItemBuilder {
        itemMeta.lore = Arrays.asList(*lore)
        return this
    }

    fun setLore(lore: List<String?>?): MyItemBuilder {
        itemMeta.lore = lore
        return this
    }

    fun insertLore(index: Int, vararg lore: String?): MyItemBuilder {
        var i = index
        val loreList = loreList
        if (loreList.isEmpty()) {
            itemMeta.lore = Arrays.asList(*lore)
        } else {
            if (i < 0) {
                i += loreList.size
            }
            loreList.addAll (i, lore.filterNotNull())
            itemMeta.lore = loreList
        }
        return this
    }

    fun insertLore(index: Int, lore: List<String>?): MyItemBuilder {
        var i = index
        val loreList = loreList
        if (loreList.isEmpty()) {
            itemMeta.lore = lore
        } else {
            if (i < 0) {
                i += loreList.size
            }
            loreList.addAll(i, lore!!)
            itemMeta.lore = loreList
        }
        return this
    }

    val loreList: MutableList<String>
        get() {
            val rawLore = itemMeta.lore
            return if (rawLore != null) ArrayList(rawLore) else ArrayList()
        }

    fun addLore(vararg lore: String?): MyItemBuilder {
        return insertLore(loreList.size, *lore)
    }

    fun addLore(lore: List<String>?): MyItemBuilder {
        return insertLore(loreList.size, lore)
    }

    fun setAmount(amount: Int): MyItemBuilder {
        item.amount = amount
        return this
    }

    fun getItem(): ItemStack {
        item.setItemMeta(itemMeta)
        return item
    }

    fun getItem(player: Player?): ItemStack {
        val lore = itemMeta.lore
        if (lore != null && PlaceholderAPIHook.enable) {
            itemMeta.lore = PlaceholderAPI.setPlaceholders(player,lore)
        }
        item.setItemMeta(itemMeta)
        return item
    }

    fun addEnchant(): MyItemBuilder {
        itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true)
        if (NMSManager.versionToInt > 7) itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        return this
    }

    companion object {
        val missingItem: ItemStack
            get() = MyItemBuilder(Material.STONE).setDisplayName(ChatColor.RED.toString() + "Missing Item!").setLore(
                ChatColor.AQUA.toString() + "If you see this line of lore"
            ).addLore(ChatColor.AQUA.toString() + "means the item has missed enchantments/material.").getItem()
    }
}

fun ItemStack.give(player: Player,amount: Int = 1){
    for (i in 1..amount){
        player.inventory.addItem(this.clone()).values.forEach{
            player.world.dropItemNaturally(player.location,it)
        }
    }
}