package cn.xgp.xgplottery.atest;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.Random;

public class Client {
    public static void main(String[] args) {
        for(int i=0; i<10;i++)
        System.out.println(new Random().nextInt(2)+1);
    }


}