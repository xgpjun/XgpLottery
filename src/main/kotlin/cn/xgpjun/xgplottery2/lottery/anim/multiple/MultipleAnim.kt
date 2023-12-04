package cn.xgpjun.xgplottery2.lottery.anim.multiple

import cn.xgpjun.xgplottery2.lottery.pojo.Award
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.AnimManager
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.InventoryHolder

abstract class MultipleAnim :InventoryHolder{
    abstract val name:String
    abstract val i18nName:String
    open val awards:MutableList<Award> = arrayListOf()
    var mustBeCrate:Boolean = false
    fun register() = AnimManager.multipleAnim.put(name,this.javaClass)
    abstract fun draw(player: Player, lottery: Lottery, crateLocation: Location?)
    open fun finish(){}
}