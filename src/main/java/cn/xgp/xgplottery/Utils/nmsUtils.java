package cn.xgp.xgplottery.Utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


public class nmsUtils {
    private static Class<?> nbtTagCompound;
    private static Class<?> itemStack;
    private static Class<?> MojangsonParser;
    private static Class<?> CraftItemStack;
    private static Method parse;
    private static Method a; //构造
    private static Method asCraftMirror;
    private static Method asNMSCopy;
    private static Method save;

    private static String NMS_PACKAGE = "";
    private static String OBC_PACKAGE = "";
    private final static String version;
    public static int versionToInt;

    static
    {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        version = packet.substring(packet.lastIndexOf('.') + 1);
        String nmsBaseHead = "net.minecraft.server.";
        versionToInt = Integer.parseInt(version.split("_")[1]);
        try {
            if(versionToInt <17)
                Class.forName(nmsBaseHead + version +".ItemStack");
            else
                Class.forName("net.minecraft.world.item.ItemStack");
            NMS_PACKAGE = nmsBaseHead + version;
            OBC_PACKAGE = "org.bukkit.craftbukkit." + version;
        } catch (ClassNotFoundException ignored){
        }

        try {
            CraftItemStack = Class.forName(OBC_PACKAGE+".inventory.CraftItemStack");
            if( versionToInt<17){
                nbtTagCompound = Class.forName(NMS_PACKAGE+ ".NBTTagCompound");
                itemStack = Class.forName(NMS_PACKAGE+".ItemStack");
                MojangsonParser = Class.forName(NMS_PACKAGE+".MojangsonParser");
                parse = MojangsonParser.getMethod("parse",String.class);
                save = itemStack.getMethod("save",nbtTagCompound);
            }else {
                nbtTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound");
                itemStack = Class.forName("net.minecraft.world.item.ItemStack");
                MojangsonParser = Class.forName("net.minecraft.nbt.MojangsonParser");
                parse = MojangsonParser.getMethod("a",String.class);
                save = itemStack.getMethod("b",nbtTagCompound);
            }
            if(versionToInt>12)
                a = itemStack.getMethod("a",nbtTagCompound);
            asCraftMirror = CraftItemStack.getMethod("asCraftMirror",itemStack);
            asNMSCopy = CraftItemStack.getMethod("asNMSCopy", ItemStack.class);



            // 后面的代码会写在这里
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public static ItemStack toItem(String NBTString){
        Object nbt; //(nbtTagCompound)
        ItemStack item = null;
        Object nmsItemStack;
        try {
            nbt = parse.invoke(MojangsonParser,NBTString);
            if(versionToInt>12){
                nmsItemStack = a.invoke(itemStack,nbt);
            }else {
                Constructor<?> constructor = itemStack.getConstructor(nbtTagCompound);
                nmsItemStack = constructor.newInstance(nbt);
            }
            item = (ItemStack) asCraftMirror.invoke(CraftItemStack,nmsItemStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }
    public static String toNBTString(ItemStack itemStack){
        Object nbt = new Object();
        try {
            Object nmsItemStack = asNMSCopy.invoke(CraftItemStack,itemStack);
            Constructor<?> constructor = nbtTagCompound.getConstructor();
            nbt =  constructor.newInstance();
            save.invoke(nmsItemStack,nbt);
        }catch (Exception e){
            e.printStackTrace();
        }

        return nbt.toString();
    }


//    public static ItemStack toItem(String NBTString){
//        Objects nbt = null; //(nbtTagCompound)
//        try {
//            nbt = MojangsonParser.parse(NBTString);
//        } catch (CommandSyntaxException e) {
//            e.printStackTrace();
//        }
//        net.minecraft.world.item.ItemStack nms = net.minecraft.world.item.ItemStack.a(nbt);
//        net.minecraft.server.v1_16_R3.ItemStack nmsItemStack = net.minecraft.server.v1_16_R3.ItemStack.a(nbt);
//        return CraftItemStack.asCraftMirror(nmsItemStack);
//    }
//    public static String toNBTString(ItemStack itemStack){
//
//        net.minecraft.server.v1_16_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
//        NBTTagCompound nbt = new NBTTagCompound();
//        nmsItemStack.save(nbt);
//        return nbt.toString();
//    }
}

