package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.common.foliaSupport.wrapper.Task
import cn.xgpjun.xgplottery2.crate.Crate
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.send
import cn.xgpjun.xgplottery2.utils.Config
import cn.xgpjun.xgplottery2.utils.isMainHand

import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.io.File
import java.util.*
import kotlin.collections.HashMap

object CrateManager {
    val cratesList = HashMap<UUID, Crate>()
    val displayItems = HashMap<UUID, Task?>()
    private val file = File(XgpLottery.instance.dataFolder,"crates.yml")

    fun register(){
        if (!file.exists()){
            return
        }
        val yaml = YamlConfiguration.loadConfiguration(file)
        yaml.getKeys(false).forEach {
            yaml.getConfigurationSection(it)?.let { section ->
                val x = section.getDouble("x")
                val y = section.getDouble("y")
                val z = section.getDouble("z")
                val worldName = section.getString("world")?:run { Message.MissingNode.get("${file.name}:$it","world");return@forEach}
                val lotteryName = section.getString("lottery")?:run { Message.MissingNode.get("${file.name}:$it","lottery"); return@forEach }
                val crateParticle = section.getString("particle")?:"DefaultParticle"
                Crate(lotteryName,x,y,z,worldName,UUID.fromString(it),crateParticle).register(UUID.fromString(it))
            }
        }
    }
    fun getLottery(location: Location):Lottery?{
        cratesList.forEach{
            if (!it.value.equal(location)){
                return@forEach
            }else{
                return LotteryManager.getLottery(it.value.lotteryName)
            }
        }
        return null
    }

    fun getUUID(location: Location):UUID?{
        cratesList.forEach {
            if (it.value.equal(location)) {
                return it.key
            }
        }
        return null
    }

    private fun save(crate: Crate, uuid: UUID){
        if (!file.exists()){
            file.createNewFile()
        }
        val yaml = YamlConfiguration.loadConfiguration(file)
        yaml.set("$uuid.x",crate.x)
        yaml.set("$uuid.y",crate.y)
        yaml.set("$uuid.z",crate.z)
        yaml.set("$uuid.world",crate.world)
        yaml.set("$uuid.lottery",crate.lotteryName)
        yaml.set("$uuid.particle",crate.crateParticle)
        yaml.save(file)
    }
    private fun create(location: Location,lottery: Lottery){
        if (getLottery(location)!=null){
            return
        }
        location.world?.name?.let {
            val uuid = UUID.randomUUID()
            val crate = Crate(lottery.name,location.x,location.y,location.z, it,uuid,"DefaultParticle")
            crate.register(uuid)
            save(crate,uuid)
        }
    }

    private fun remove(uuid: UUID){
        cratesList.remove(uuid)
        if (!file.exists()){
            file.createNewFile()
            return
        }
        val yaml = YamlConfiguration.loadConfiguration(file)
        yaml.set("$uuid",null)
        yaml.save(file)
    }

    fun createCrate(player: Player,lottery: Lottery){
        Message.CreateCrate.get(lottery.name).send(player)
        val listener = object :Listener{
            val uuid = player.uniqueId
            @EventHandler
            fun select(e:PlayerInteractEvent){
                val ePlayer = e.player
                if (ePlayer.uniqueId == uuid &&e.isMainHand()){
                    e.isCancelled = true
                    if (e.action == Action.LEFT_CLICK_BLOCK){
                        e.clickedBlock?.location?.let { location->
                            getLottery(location)?.let {
                                //已存在
                                Message.CrateExisted.get().send(ePlayer)
                            }?:run {
                                //不存在 , 创建
                                create(location,lottery)
                                Message.CreateCrateSuccessfully.get().send(ePlayer)
                            }
                            HandlerList.unregisterAll(this)
                        }
                    }
                }
            }
        }
        listener.register()
    }

    fun removeCrate(player: Player){
        Message.RemoveCrate.get().send(player)
        val listener = object :Listener{
            val uuid = player.uniqueId
            @EventHandler
            fun remove(e:PlayerInteractEvent){
                val ePlayer = e.player
                if (ePlayer.uniqueId == uuid&&e.isMainHand()){
                    e.isCancelled = true
                    if (e.action == Action.LEFT_CLICK_BLOCK){
                        e.clickedBlock?.location?.let { location ->
                            getUUID(location)?.let {
                                //存在 ， 移除
                                remove(it)
                            }?:run {
                                //不存在
                                Message.RemoveFailed.get().send(ePlayer)
                            }
                            HandlerList.unregisterAll(this)
                        }
                    }
                }
            }
        }
        listener.register()
    }

}
fun Crate.register(uuid: UUID){
    CrateManager.cratesList[uuid] = this
}