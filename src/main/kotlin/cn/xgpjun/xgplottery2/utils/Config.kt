package cn.xgpjun.xgplottery2.utils

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.send
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import java.util.*

object Config {
    var version = "2.0.0"
    var checkFullInventory = false
    var debug = false
    private var uuid : UUID? = null
    private var read = false
    fun loadConfig(){
        XgpLottery.instance.saveDefaultConfig()
        XgpLottery.instance.config.let {
            version = it.getString("version","2.0.0").toString()
            checkFullInventory = it.getBoolean("checkFullInventory",false)
            debug = it.getBoolean("debug",false)
            uuid = it.getString("uuid")?.let { string ->
                UUID.fromString(string)
            }
        }
    }

    fun readWiki(sender: CommandSender):Boolean{
        if (this.read){
            return true
        }

        val read = uuid?.equals(UUID.fromString("62a79341-7adb-4791-a1c4-d829f237f033")) == true
        if (!read){
            val url = "https://xgpjun.github.io/XL-wiki/intro"
            try {
                "&cno-support".send(sender)
                val text = TextComponent("&3Please READ: &7&l&n$url".color())
                text.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL,url)
                sender.spigot().sendMessage(text)
            }catch (e:Exception){
                sender.sendMessage("&3Please READ: &7&l&n$url".color())
            }
        }else{
            this.read = true
        }

        return read
    }
}