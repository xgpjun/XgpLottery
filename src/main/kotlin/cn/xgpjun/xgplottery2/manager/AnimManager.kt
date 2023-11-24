package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.lottery.anim.multiple.MultipleAnim
import cn.xgpjun.xgplottery2.lottery.anim.multiple.impl.DefaultMultipleAnim
import cn.xgpjun.xgplottery2.lottery.anim.single.SingleAnim
import cn.xgpjun.xgplottery2.lottery.anim.single.impl.DefaultSingleAnim

object AnimManager {
    val singleAnim = HashMap<String,Class<out SingleAnim>>()
    val multipleAnim = HashMap<String,Class<out MultipleAnim>>()

    fun register(){
        DefaultSingleAnim().register()
    }

    fun getSingleI18Name(name:String):String{
        return singleAnim[name]?.getDeclaredConstructor()?.newInstance()?.i18Name?:""
    }
    fun getMultipleI18Name(name:String):String{
        return multipleAnim[name]?.getDeclaredConstructor()?.newInstance()?.i18Name?:""
    }

    fun getSingleAnimObject(name: String) = singleAnim[name]?.getConstructor()?.newInstance()?:DefaultSingleAnim()


    fun getMultipleAnimObject(name:String) = multipleAnim[name]?.getConstructor()?.newInstance()?:DefaultMultipleAnim()

}

fun Class<out SingleAnim>.new(): SingleAnim = this.getDeclaredConstructor().newInstance()