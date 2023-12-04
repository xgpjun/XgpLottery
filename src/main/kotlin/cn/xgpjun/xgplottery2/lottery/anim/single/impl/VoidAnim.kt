package cn.xgpjun.xgplottery2.lottery.anim.single.impl

import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.lottery.anim.single.SingleAnim
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.Message
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class VoidAnim:SingleAnim() {
    override val name: String
        get() = "Void"
    override val i18nName: String
        get() = Message.VoidAnimName.get().color()

    override fun draw(player: Player, lottery: Lottery, crateLocation: Location?) {
        award?.give(player)
    }

    override fun getInventory(): Inventory {
        return Bukkit.createInventory(this,54)
    }
}