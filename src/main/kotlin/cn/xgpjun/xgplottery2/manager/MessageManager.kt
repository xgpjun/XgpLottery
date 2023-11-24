package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.XgpLottery
import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.function.UnaryOperator

object MessageManager {
    private var config:YamlConfiguration? = null

    fun load(){
        val file =File(XgpLottery.instance.dataFolder,"message.yml")
        config = if (file.exists()){
            YamlConfiguration.loadConfiguration(file)
        }else{
            XgpLottery.instance.saveResource("message.yml",false)
            YamlConfiguration.loadConfiguration(file)
        }
        Message.entries.forEach {it.string = loadMessage(it.path) }

        MessageL.entries.forEach{it.stringList = loadMessageList(it.path)}
    }
    private fun loadMessage(path:String):String{
        return config?.getString(path)?: "${ChatColor.RED}Missing Message:$path"
    }
    private fun loadMessageList(path:String):MutableList<String>{
        return config?.getStringList(path)?: arrayListOf("${ChatColor.RED}Missing Message:$path")
    }


}

enum class Message(val path: String){
    MissingNode("error.missingNode") {
        override fun get(vararg placeholders: String?): String {
            return string.replace("{file.name}",placeholders[0].toString()).replace("{node}",placeholders[1].toString())
        }
                                     },
    AwardNull("error.awardNull"),
    Prefix("message.prefix"),
    OnlyPlayer("error.onlyPlayer"),
    NotFoundLottery("error.notFoundLottery"),
    CrateExisted("error.crateExisted"),
    RemoveFailed("error.removeFailed"),
    NotSupport("error.notSupport"),
    NotFoundPlayer("error.notFoundPlayer"),
    CreateCrateSuccessfully("message.createCrateSuccessfully"),
    CreateCrate("message.createCrate"){
        override fun get(vararg placeholders: String?): String {
            return string.replace("{name}",placeholders[0].toString())
        }
                                      },
    RemoveCrate("message.removeCrate"),
    ManageTitle("gui.admin.manage.title"),

    ItemBorderName( "gui.items.border.name"),
    ItemBorder2Name( "gui.items.border2.name"),
    ItemExitName("gui.items.exit.name"),
    ItemPreviousName("gui.items.previous.name"),
    ItemNextPageName("gui.items.nextPage.name"),
    ItemPreviousPageName("gui.items.previousPage.name"),

    SingleAnimChange("gui.message.singleAnimChange"){
        override fun get(vararg placeholders: String?): String {
            return string.replace("{name}",placeholders[0].toString())
        }
                                                    },
    MultipleAnimChange("gui.message.multipleAnimChange"){
        override fun get(vararg placeholders: String?): String {
            return string.replace("{name}",placeholders[0].toString())
        }
                                                        },
    ValueChange("gui.message.valueChange"),
    SellTypeChange("gui.message.sellTypeChange"){
        override fun get(vararg placeholders: String?): String {
            return string.replace("{name}",placeholders[0].toString())
        }
                                                },
    Cancel("gui.message.cancel"),
    Point("noun.point"),
    Money("noun.money"),
    EXP("noun.exp"),


    //动画
    DefaultSingleAnimTitle("gui.singleAnim.default.title"),
    DefaultSingleAnimName("gui.singleAnim.default.name"),
    ;

    var string:String = "${ChatColor.RED}Not Init Yet:${name}"

    open fun get(vararg placeholders:String?):String{
        return string
    }
}

enum class MessageL(val path: String){
    HelpMessage("help.help"),
    CrateHelp("help.crate"),
    DrawHelp("help.draw"),
    ItemBorderLore("gui.items.border.lore"),
    ItemBorder2Lore("gui.items.border2.lore"),
    CrateListInfo("message.crateListInfo"){
        override fun get(vararg placeholders: String?): MutableList<String> {
            val result = ArrayList<String>()
            stringList.forEach{
                val string = it
                    .replace("{world}",placeholders[0].toString())
                    .replace("{x}",placeholders[1].toString())
                    .replace("{y}",placeholders[2].toString())
                    .replace("{z}",placeholders[3].toString())
                    .replace("{lotteryName}",placeholders[4].toString())
                result.add(string)
            }
            return result
        }
                                          },
    ;
    var stringList:MutableList<String> = mutableListOf("${ChatColor.RED}Not Init Yet")
    open fun get(vararg placeholders:String?):MutableList<String>{
        return stringList
    }
}

private fun String?.toString():String{
    return this?:"noValue"
}
