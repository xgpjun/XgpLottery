package cn.xgpjun.xgplottery2.nms

import cn.xgpjun.xgplottery2.manager.NMSWrapper
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Constructor
import java.lang.reflect.Method

object V1_16Below:NMSWrapper(){
    override lateinit var nbtTagCompound: Class<*>
    override lateinit var nmsItemStack: Class<*>
    override lateinit var mojangsonParser: Class<*>
    override lateinit var parse: Method
    override lateinit var a: Method
    override lateinit var save: Method
    override lateinit var getTag: Method
    override lateinit var setTag: Method
    override lateinit var setString: Method
    override lateinit var getString: Method

    lateinit var createStack:Method
    lateinit var newItemStack:Constructor<*>
    override lateinit var newNBT: Constructor<*>

    override fun init() {
        nbtTagCompound = Class.forName("$NMS_PACKAGE.NBTTagCompound")
        nmsItemStack = Class.forName("$NMS_PACKAGE.ItemStack")
        mojangsonParser = Class.forName("$NMS_PACKAGE.MojangsonParser")
        newNBT = nbtTagCompound.getConstructor()

        parse = mojangsonParser.getMethod("parse", String::class.java)
        save = nmsItemStack.getMethod("save", nbtTagCompound)
        getTag = nmsItemStack.getMethod("getTag")
        setTag = nmsItemStack.getMethod("setTag", nbtTagCompound)
        setString = nbtTagCompound.getMethod("setString",String::class.java, String::class.java)
        getString = nbtTagCompound.getMethod("getString",String::class.java)
        if (versionToInt>12)
            a = nmsItemStack.getMethod("a", nbtTagCompound)
        if (versionToInt<9)
            createStack = nmsItemStack.getMethod("createStack", nbtTagCompound)
        else
            newItemStack = nmsItemStack.getConstructor(nbtTagCompound)

        handle?.isAccessible = true
    }

    override fun toItem(nbtString: String):ItemStack {
        val nbt = parse.invoke(mojangsonParser,nbtString) //(nbtTagCompound)
        val item = if(versionToInt>12){
            a.invoke(nmsItemStack,nbt)
        }else if(versionToInt>9){
            newItemStack.newInstance(nbt)
        }else{
            createStack.invoke(nmsItemStack,nbt)
        }
        return asCraftMirror.invoke(craftItemStack,item) as ItemStack
    }
}

object V1_17 :NMSWrapper(){
    override lateinit var nbtTagCompound: Class<*>
    override lateinit var nmsItemStack: Class<*>
    override lateinit var mojangsonParser: Class<*>
    override lateinit var parse: Method
    override lateinit var a: Method
    override lateinit var save: Method
    override lateinit var getTag: Method
    override lateinit var setTag: Method
    override lateinit var setString: Method
    override lateinit var getString: Method
    override lateinit var newNBT: Constructor<*>

    override fun init() {
        nbtTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound")
        nmsItemStack = Class.forName("net.minecraft.world.item.ItemStack")
        mojangsonParser = Class.forName("net.minecraft.nbt.MojangsonParser")
        newNBT = nbtTagCompound.getConstructor()

        parse = mojangsonParser.getMethod("a", String::class.java)
        save = nmsItemStack.getMethod("b", nbtTagCompound)
        getTag = nmsItemStack.getMethod("getOrCreateTag")
        setTag = nmsItemStack.getMethod("setTag", nbtTagCompound)
        setString = nbtTagCompound.getMethod("a",String::class.java, String::class.java)
        getString = nbtTagCompound.getMethod("l",String::class.java)
        a = nmsItemStack.getMethod("a", nbtTagCompound)
        handle?.isAccessible = true

    }

    override fun toItem(nbtString: String):ItemStack {
        val nbt = parse.invoke(mojangsonParser,nbtString) //(nbtTagCompound)
        val item = a.invoke(nmsItemStack,nbt)
        return asCraftMirror.invoke(craftItemStack,item) as ItemStack
    }
}

object V1_18 :NMSWrapper(){
    override lateinit var nbtTagCompound: Class<*>
    override lateinit var nmsItemStack: Class<*>
    override lateinit var mojangsonParser: Class<*>
    override lateinit var parse: Method
    override lateinit var a: Method
    override lateinit var save: Method
    override lateinit var getTag: Method
    override lateinit var setTag: Method
    override lateinit var setString: Method
    override lateinit var getString: Method
    override lateinit var newNBT: Constructor<*>

    override fun init() {
        nbtTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound")
        nmsItemStack = Class.forName("net.minecraft.world.item.ItemStack")
        mojangsonParser = Class.forName("net.minecraft.nbt.MojangsonParser")
        newNBT = nbtTagCompound.getConstructor()

        parse = mojangsonParser.getMethod("a", String::class.java)
        save = nmsItemStack.getMethod("b", nbtTagCompound)
        getTag = nmsItemStack.getMethod("u")
        setTag = nmsItemStack.getMethod("c", nbtTagCompound)
        setString = nbtTagCompound.getMethod("a",String::class.java, String::class.java)
        getString = nbtTagCompound.getMethod("l",String::class.java)
        a = nmsItemStack.getMethod("a", nbtTagCompound)
        handle?.isAccessible = true

    }

    override fun toItem(nbtString: String):ItemStack {
        val nbt = parse.invoke(mojangsonParser,nbtString) //(nbtTagCompound)
        val item = a.invoke(nmsItemStack,nbt)
        return asCraftMirror.invoke(craftItemStack,item) as ItemStack
    }
}

object V1_19 :NMSWrapper(){
    override lateinit var nbtTagCompound: Class<*>
    override lateinit var nmsItemStack: Class<*>
    override lateinit var mojangsonParser: Class<*>
    override lateinit var parse: Method
    override lateinit var a: Method
    override lateinit var save: Method
    override lateinit var getTag: Method
    override lateinit var setTag: Method
    override lateinit var setString: Method
    override lateinit var getString: Method
    override lateinit var newNBT: Constructor<*>

    override fun init() {
        nbtTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound")
        nmsItemStack = Class.forName("net.minecraft.world.item.ItemStack")
        mojangsonParser = Class.forName("net.minecraft.nbt.MojangsonParser")
        newNBT = nbtTagCompound.getConstructor()

        parse = mojangsonParser.getMethod("a", String::class.java)
        save = nmsItemStack.getMethod("b", nbtTagCompound)
        getTag = nmsItemStack.getMethod("v")
        setTag = nmsItemStack.getMethod("c", nbtTagCompound)
        setString = nbtTagCompound.getMethod("a",String::class.java, String::class.java)
        getString = nbtTagCompound.getMethod("l",String::class.java)
        a = nmsItemStack.getMethod("a", nbtTagCompound)
        handle?.isAccessible = true

    }

    override fun toItem(nbtString: String):ItemStack {
        val nbt = parse.invoke(mojangsonParser,nbtString) //(nbtTagCompound)
        val item = a.invoke(nmsItemStack,nbt)
        return asCraftMirror.invoke(craftItemStack,item) as ItemStack
    }
}

object V1_20 :NMSWrapper(){
    override lateinit var nbtTagCompound: Class<*>
    override lateinit var nmsItemStack: Class<*>
    override lateinit var mojangsonParser: Class<*>
    override lateinit var parse: Method
    override lateinit var a: Method
    override lateinit var save: Method
    override lateinit var getTag: Method
    override lateinit var setTag: Method
    override lateinit var setString: Method
    override lateinit var getString: Method
    override lateinit var newNBT: Constructor<*>

    override fun init() {
        nbtTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound")
        nmsItemStack = Class.forName("net.minecraft.world.item.ItemStack")
        mojangsonParser = Class.forName("net.minecraft.nbt.MojangsonParser")
        newNBT = nbtTagCompound.getConstructor()
        parse = mojangsonParser.getMethod("a", String::class.java)
        save = nmsItemStack.getMethod("b", nbtTagCompound)
        getTag = nmsItemStack.getMethod("w")
        setTag = nmsItemStack.getMethod("c", nbtTagCompound)
        setString = nbtTagCompound.getMethod("a",String::class.java, String::class.java)
        getString = nbtTagCompound.getMethod("l",String::class.java)
        a = nmsItemStack.getMethod("a", nbtTagCompound)
        handle?.isAccessible = true

    }

    override fun toItem(nbtString: String):ItemStack {
        val nbt = parse.invoke(mojangsonParser,nbtString) //(nbtTagCompound)
        val item = a.invoke(nmsItemStack,nbt)
        return asCraftMirror.invoke(craftItemStack,item) as ItemStack
    }
}