package cn.xgpjun.xgplottery2.lottery.pojo

import cn.xgpjun.xgplottery2.command.sub.Draw
import cn.xgpjun.xgplottery2.lottery.calculator.impl.NormalCalculator
import cn.xgpjun.xgplottery2.lottery.enums.SellType
import cn.xgpjun.xgplottery2.manager.AnimManager
import cn.xgpjun.xgplottery2.manager.DrawManager
import cn.xgpjun.xgplottery2.manager.SchedulerManager
import cn.xgpjun.xgplottery2.manager.toNBTString
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File
import java.util.*

class Lottery(
    var name: String,
    animation:String,
    multipleAnimation:String,
    value:Double,
    sellType: SellType,
    var virtualKeyName: String,
    var customTags:MutableMap<String,Any>,
    key:ItemStack,
    var awards:MutableMap<String,Award>,
    calculator: String,
    private var file: File,
){
    private var yaml = YamlConfiguration.loadConfiguration(file)
    var animation = animation
        set(value) {
            field = value
            yaml.set("animation",value)
            yaml.save(file)
        }
    var multipleAnimation = multipleAnimation
        set(value) {
            field = value
            yaml.set("multipleAnimation",value)
            yaml.save(file)

        }
    var value = value
        set(value) {
            field = value
            yaml.set("value",value)
            yaml.save(file)

        }
    var sellType = sellType
        set(value) {
            field = value
            yaml.set("sellType",value)
            yaml.save(file)

        }
    var key = key
        set(value) {
            field = value
            yaml.set("key.internal",value.toNBTString())
            yaml.save(file)

        }
    var calculator = calculator
        set(value) {
            field = value
            yaml.set("calculator",value)
            yaml.save(file)
        }
    fun createNewAward(name: String){
        awards[name] = Award.create(name)

        yaml.set("awards.$name.item.material","STONE")
        yaml.set("awards.$name.item.displayName","&6special stone")
        yaml.set("awards.$name.item.amount",1)
        yaml.set("awards.$name.giveItem",true)
        yaml.set("awards.$name.broadcast",false)
        yaml.set("awards.$name.weight",1)
        yaml.save(file)
    }
    fun getTotalWeight() = awards.values.sumOf { it.weight }

    fun multipleDraw(player: Player){
        SchedulerManager.getScheduler().runTaskAsynchronously {
            val animation = AnimManager.getMultipleAnimObject(multipleAnimation)
            val calculator = DrawManager.calculators[calculator]?:NormalCalculator()
            animation.awards.clear()
            for (i in 0..9){
                calculator.getAward(player,this)?.let {
                    animation.awards.add(it)
                }
            }
            SchedulerManager.getScheduler().runTask{
                animation.draw(player, this, null)
            }
        }

    }

    fun singleDraw(player: Player){
        SchedulerManager.getScheduler().runTaskAsynchronously {
            val animation = AnimManager.getSingleAnimObject(animation)
            val calculator = DrawManager.calculators[calculator] ?: NormalCalculator()
            animation.award = calculator.getAward(player, this)
            SchedulerManager.getScheduler().runTask{
                animation.draw(player, this, null)
            }
        }
    }

    fun getRandomAward(): Award? {
        val next = Random().nextInt(awards.size)
        return awards.values.toList().getOrNull(next)
    }

}
