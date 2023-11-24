package cn.xgpjun.xgplottery2.lottery.enums

import cn.xgpjun.xgplottery2.manager.Message


enum class SellType (s:String){
    POINTS("points"),
    MONEY("money"),
    EXP("exp"),
    NULL("null");

    private val sellType: String = s

    fun getName():String{
        return when(this){
            POINTS -> Message.Point.get()
            MONEY -> Message.Money.get()
            EXP -> Message.EXP.get()
            NULL -> "none"
        }
    }

    companion object {
        fun fromString(value: String): SellType {
            for (type in entries) {
                if (type.sellType.equals(value, ignoreCase = true)) {
                    return type
                }
            }
            return NULL
        }
    }
}
