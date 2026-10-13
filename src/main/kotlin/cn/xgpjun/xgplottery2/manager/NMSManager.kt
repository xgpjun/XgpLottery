package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.XgpLottery
import de.tr7zw.nbtapi.NBT
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Retains the version checks used by the legacy material/sound adapters.
 * Item data is handled by NBTAPI, without accessing CraftBukkit package names.
 */
object NMSManager {
    val version: String = Bukkit.getBukkitVersion().substringBefore('-')
    val versionToInt: Int = version.split('.').let { parts ->
        val major = parts[0].toInt()
        if (major == 1) parts[1].toInt() else major
    }

    fun register(): Boolean {
        if (!NBT.preloadApi()) {
            XgpLottery.instance.logger.severe("NBTAPI could not initialize for Minecraft $version. Please update NBTAPI.")
            Bukkit.getPluginManager().disablePlugin(XgpLottery.instance)
            return false
        }
        return true
    }
}

/** NBTAPI stores these plugin tags in minecraft:custom_data on 1.20.5+. */
fun ItemStack.setTag(key: String, value: String): ItemStack {
    if (type == Material.AIR || amount <= 0) return this
    NBT.modify<Unit>(this) { nbt -> nbt.setString(key, value) }
    return this
}

fun ItemStack.getTag(key: String): String? {
    if (type == Material.AIR || amount <= 0) return null
    return NBT.get<String?>(this) { nbt ->
        if (nbt.hasTag(key)) nbt.getString(key) else null
    }
}

/** Serializes the entire item, including components and its DataVersion. */
fun ItemStack.toNBTString(): String = NBT.itemStackToNBT(this).toString()

/**
 * NBTAPI upgrades older DataVersions and legacy Count/tag data to components
 * on 1.20.5+. Keep the existing YAML internal fields readable.
 */
fun String.toItemStack(): ItemStack {
    return requireNotNull(NBT.itemStackFromNBT(NBT.parseNBT(this))) {
        "Unable to deserialize an item from its internal NBT data."
    }
}
