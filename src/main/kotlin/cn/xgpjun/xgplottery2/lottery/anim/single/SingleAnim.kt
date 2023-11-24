package cn.xgpjun.xgplottery2.lottery.anim.single

import cn.xgpjun.xgplottery2.lottery.pojo.Award
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.AnimManager
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.InventoryHolder

abstract class SingleAnim:InventoryHolder {
    abstract val name:String
    abstract val i18Name:String
    var award:Award? = null
    var mustBeCrate:Boolean = false
    abstract fun draw(player: Player,lottery: Lottery,crateLocation:Location?)
    fun register() = AnimManager.singleAnim.put(name,this.javaClass)
    open fun finish(){}
}