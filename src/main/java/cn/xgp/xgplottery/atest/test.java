package cn.xgp.xgplottery.atest;



public class test {
    public static test instance;
    public static String EnableMessage = "null";

    public static void Load(String fileName){
        EnableMessage = fileName;
    }

    public  test(){

        instance = this;
    }
}
