package cn.xgpjun.xgplottery2.manager

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.nms.*
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

object NMSManager {
    lateinit var nmsWrapper:NMSWrapper

    private var packet: String = Bukkit.getServer().javaClass.getPackage().name
    var version: String = packet.substring(packet.lastIndexOf('.') + 1)
    var versionToInt = version.split("_")[1].toInt()
    fun register(){
        try {
            nmsWrapper = when(versionToInt){
                20 -> V1_20
                19 -> V1_19
                18 -> V1_18
                17 -> V1_17
                else -> {V1_16Below}
            }
        }catch (e:Exception){
            e.printStackTrace()
            Bukkit.getPluginManager().disablePlugin(XgpLottery.instance)
        }
        nmsWrapper.init()
    }
}

abstract class NMSWrapper() {
    abstract val nbtTagCompound: Class<*>
    abstract val newNBT:Constructor<*>
    abstract val nmsItemStack: Class<*>
    abstract val mojangsonParser: Class<*>
    val craftItemStack: Class<*> by lazy {
        Class.forName("$OBC_PACKAGE.inventory.CraftItemStack")
    }

    val handle:Field? by lazy {
        try {
            craftItemStack.getDeclaredField("handle")
        }catch (e:Exception){
            null
        }
    }
    abstract val parse: Method
    abstract val a: Method //构造

    val asCraftMirror: Method by lazy {
        craftItemStack.getMethod("asCraftMirror",nmsItemStack)
    }
    val asNMSCopy: Method by lazy {
        craftItemStack.getMethod("asNMSCopy", ItemStack::class.java)
    }
    abstract val save: Method
    val toString: Method by lazy {
        nbtTagCompound.getMethod("toString")
    }
    abstract val getTag: Method
    abstract val setTag: Method //1.7.10

    abstract val setString: Method
    abstract val getString: Method

    val NMS_PACKAGE :String = "net.minecraft.server.$version"
    val OBC_PACKAGE :String = "org.bukkit.craftbukkit.$version"
    val version:String
        get() {return NMSManager.version}
    val versionToInt :Int
        get() {return NMSManager.versionToInt}


    abstract fun init()
    abstract fun toItem(nbtString: String):ItemStack

    fun toNbtString(itemStack: ItemStack):String{
        val nmsItemStack =if (handle!=null&&craftItemStack.isInstance(itemStack)) {
            handle!!.get(itemStack)
        }else{
            asNMSCopy.invoke(craftItemStack,itemStack)
        }
        val nbt = newNBT.newInstance()
        save.invoke(nmsItemStack,nbt)
        return toString.invoke(nbt) as String
    }


    fun getTag(itemStack: ItemStack,key:String):String?{
        val nmsItemStack =if (handle!=null&&craftItemStack.isInstance(itemStack)) {
            handle!!.get(itemStack)
        }else{
            asNMSCopy.invoke(craftItemStack,itemStack)
        }
        val tags = getTag.invoke(nmsItemStack)?:return null
        return getString.invoke(tags,key) as String
    }
    fun setTag(itemStack: ItemStack,key: String,value:String):ItemStack{
        return if (handle!=null&&craftItemStack.isInstance(itemStack)) {
            val nmsItemStack = handle!!.get(itemStack)
            val tags = getTag.invoke(nmsItemStack)?:newNBT.newInstance()
            setString.invoke(tags,key,value)
            itemStack
        }else{
            val nmsItemStack = asNMSCopy.invoke(craftItemStack,itemStack)
            val tags = getTag.invoke(nmsItemStack)?:newNBT.newInstance()
            setString.invoke(tags,key,value)
            setTag.invoke(nmsItemStack,tags)
            asCraftMirror.invoke(craftItemStack,nmsItemStack) as ItemStack
        }

//            setTag.invoke(nmsItemStack,tags)
//            return asCraftMirror.invoke(craftItemStack,nmsItemStack) as ItemStack
    }
}
fun ItemStack.setTag(key: String,value: String):ItemStack{
    return NMSManager.nmsWrapper.setTag(this,key,value)
}

fun ItemStack.getTag(key:String):String?{
    return NMSManager.nmsWrapper.getTag(this,key)
}

fun ItemStack.toNBTString():String{
    return NMSManager.nmsWrapper.toNbtString(this)
}
fun String.toItemStack():ItemStack{
    return NMSManager.nmsWrapper.toItem(this);
}
