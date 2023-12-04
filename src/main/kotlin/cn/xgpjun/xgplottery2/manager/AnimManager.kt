package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.lottery.anim.multiple.MultipleAnim
import cn.xgpjun.xgplottery2.lottery.anim.multiple.impl.BoxMultipleAnim
import cn.xgpjun.xgplottery2.lottery.anim.multiple.impl.DefaultMultipleAnim
import cn.xgpjun.xgplottery2.lottery.anim.multiple.impl.SelectMultipleAnim
import cn.xgpjun.xgplottery2.lottery.anim.multiple.impl.SimpleMultipleAnim
import cn.xgpjun.xgplottery2.lottery.anim.single.SingleAnim
import cn.xgpjun.xgplottery2.lottery.anim.single.impl.*

object AnimManager {
    val singleAnim = HashMap<String,Class<out SingleAnim>>()
    val multipleAnim = HashMap<String,Class<out MultipleAnim>>()

    fun register(){
        DefaultAnim().register()
        ColorfulAnim().register()
        MarqueeAnim().register()
        SelectAnim().register()
        VoidAnim().register()

        DefaultMultipleAnim().register()
        BoxMultipleAnim().register()
        SelectMultipleAnim().register()
        SimpleMultipleAnim().register()
    }

    fun getSingleI18Name(name:String):String{
        return singleAnim[name]?.getDeclaredConstructor()?.newInstance()?.i18nName?:""
    }
    fun getMultipleI18Name(name:String):String{
        return multipleAnim[name]?.getDeclaredConstructor()?.newInstance()?.i18nName?:""
    }

    fun getSingleAnimObject(name: String) = singleAnim[name]?.getConstructor()?.newInstance()?:DefaultAnim()


    fun getMultipleAnimObject(name:String) = multipleAnim[name]?.getConstructor()?.newInstance()?:DefaultMultipleAnim()

}

fun Class<out SingleAnim>.new(): SingleAnim = this.getDeclaredConstructor().newInstance()