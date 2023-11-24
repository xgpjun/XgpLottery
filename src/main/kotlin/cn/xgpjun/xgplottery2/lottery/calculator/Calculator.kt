package cn.xgpjun.xgplottery2.lottery.calculator

import cn.xgpjun.xgplottery2.lottery.pojo.Award
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.DrawManager
import org.bukkit.entity.Player

abstract class Calculator {
    abstract fun getAward(player: Player,lottery: Lottery):Award?
    fun register(name:String){
        DrawManager.calculators[name] = this
    }
}