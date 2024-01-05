package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.log
import cn.xgpjun.xgplottery2.lottery.enums.SellType
import cn.xgpjun.xgplottery2.lottery.pojo.Award
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import dev.lone.itemsadder.api.CustomStack
import io.lumine.mythic.api.MythicProvider
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.manager.ItemManager
import java.io.File
import java.util.*

object LotteryManager {
    val lotteryMap = HashMap<String,Lottery>()
    var ia = false
    var ni = false
    var mm = false
    fun loadFileData(){

        val folder = File(XgpLottery.instance.dataFolder,"lottery")
        if (!folder.exists()) {
            return
        }
        ia = Bukkit.getPluginManager().getPlugin("ItemsAdder") != null
        ni = Bukkit.getPluginManager().getPlugin("NeigeItems") != null
        mm = Bukkit.getPluginManager().getPlugin("MythicMobs") != null

        val yamlFiles: Array<out File>? = folder.listFiles { _:File, name:String ->
            name.lowercase(Locale.getDefault()).endsWith(".yml")
        }
        yamlFiles?.forEach { file: File ->
            val yaml = YamlConfiguration.loadConfiguration(file)
            val name = file.name.replace(".yml","")
            val calculator = yaml.getString("calculator")?:run { Message.MissingNode.get(file.name,"calculator").log();return@forEach }
            val animation = yaml.getString("animation")?: run { Message.MissingNode.get(file.name,"animation").log() ;return@forEach}
            val multipleAnimation = yaml.getString("multipleAnimation")?: run { Message.MissingNode.get(file.name,"multipleAnimation").log();return@forEach }
            val value = yaml.getDouble("value",0.0)
            val sellType = SellType.fromString(yaml.getString("sellType")?:"null")
            val virtualKeyName = yaml.getString("virtualKeyName",name)!!
            val showProbability = yaml.getBoolean("showProbability",false)
            val lotteryCustomTag: HashMap<String, Any> = yaml.getConfigurationSection("customTag")?.getKeys(false)?.run {
                val tags = HashMap<String,Any>()
                for (ss in this){
                    tags[ss] = yaml.get("customTag.$ss") as Any
                }
                tags
            }?:HashMap()

            //Build key's itemStack
            val keySection = yaml.getConfigurationSection("key")

            val key:ItemStack = keySection?.let {
                if (it.get("internal")!=null){
                    keySection.getString("internal")?.toItemStack()
                }else if(ni&&it.getString("ni")!=null){
                    ItemManager.getItemStack(it.getString("ni")!!)
                }else if(mm&&it.get("mm")!=null){
                    MythicProvider.get().itemManager.getItem(it.getString("mm")).get().cachedBaseItem
                }else if(ia&&it.get("ia")!=null){
                    CustomStack.getInstance(it.getString("ia"))?.itemStack
                }else{
                    it.run{
                        val keyMaterial = keySection.getString("material")
                            ?.let { Material.getMaterial(it.uppercase(Locale.getDefault())) }
                            ?:Material.STONE
                        val keyDisplayName = keySection.getColorString("displayName")
                        val keyLore = keySection.getColorStringList("lore")
                        MyItemBuilder(keyMaterial).setDisplayName(keyDisplayName).setLore(keyLore).getItem()
                    }                }
            }?:run { MyItemBuilder.missingItem }
            val awards = getAwards(yaml)

            val lottery = Lottery(name,animation,multipleAnimation,value,sellType,virtualKeyName,showProbability,lotteryCustomTag,key,awards,calculator,file)
            lotteryMap[name] = lottery
        }
    }

    fun getLottery(lotteryName:String) = lotteryMap[lotteryName]

    fun getAwards(yaml: YamlConfiguration):MutableMap<String,Award>{
        return yaml.getConfigurationSection("awards")?.getKeys(false)?. run {
            val awardMap = HashMap<String, Award>()
            for (s in this){
                val item = yaml.getConfigurationSection("awards.$s")?.getConfigurationSection("item")?.run {
                    //Build itemStack
                    if (this.get("internal")!=null){
                        this.getString("internal")?.toItemStack()
                    }else if(ni &&this.getString("ni")!=null){
                        ItemManager.getItemStack(this.getString("ni")!!)
                    }else if(mm &&this.get("mm")!=null){
                        MythicProvider.get().itemManager.getItem(this.getString("mm")).get().cachedBaseItem
                    }else if(ia &&this.get("ia")!=null){
                        CustomStack.getInstance(this.getString("ia"))?.itemStack
                    }else{
                        val material = this.getString("material")
                            ?.let { Material.getMaterial(it.uppercase(Locale.getDefault())) }
                            ?: Material.STONE
                        val displayName = this.getColorString("displayName")
                        val lore = this.getColorStringList("lore")
                        val amount = this.getInt("amount",1)
                        MyItemBuilder(material).setDisplayName(displayName).setLore(lore).setAmount(amount).getItem()
                    }
                } ?: continue
                val awardSetting : ConfigurationSection = yaml.getConfigurationSection("awards.$s")!!
                val giveItem = awardSetting.getBoolean("giveItem",true)
                val weight = awardSetting.getInt("weight",1)
                val command = awardSetting.getStringList("command")
                val customTag = awardSetting.getConfigurationSection("customTag")?.getKeys(false)?.run {
                    val tags = HashMap<String, Any>()
                    for (ss in this){
                        tags[ss] = awardSetting.get("customTag.$ss") as Any
                    }
                    tags
                }?: HashMap()
                val rawItem = awardSetting.getBoolean("rawItem",false)
                val award = Award(s,item,giveItem,weight,rawItem,command,customTag)
                awardMap[s] = award
            }
            awardMap
        }?: HashMap<String, Award>()
    }
}




fun ConfigurationSection.getColorString(path:String):String?{
    return this.getString(path)?.run{this.color()}

}
fun ConfigurationSection.getColorStringList(path:String): MutableList<String> {
    val list = this.getStringList(path)
    val coloredList = mutableListOf<String>()
     list.forEach {
         coloredList.add(it.color())
    }
    return coloredList
}


