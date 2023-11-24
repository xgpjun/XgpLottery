package cn.xgpjun.xgplottery2.lottery.anim.multiple.impl

import cn.xgpjun.xgplottery2.lottery.anim.multiple.MultipleAnim
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class DefaultMultipleAnim:MultipleAnim() {

    override val name: String
        get() = TODO("Not yet implemented")
    override val i18Name: String
        get() = TODO("Not yet implemented")

    override fun draw(player: Player, lottery: Lottery, crateLocation: Location?) {
        TODO("Not yet implemented")
    }

    override fun getInventory(): Inventory {
        TODO("Not yet implemented")
    }
}