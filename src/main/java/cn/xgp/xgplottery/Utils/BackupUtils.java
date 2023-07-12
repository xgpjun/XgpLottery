package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.XgpLottery;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BackupUtils {
    public static void backup(){
        backupFiles();
    }

    private static void backupFiles(){
        File fold = XgpLottery.instance.getDataFolder();
        File backup = new File(fold,"backup");
        if(backup.exists()){
            //删除
            XgpLottery.log("正在删除过旧的备份！");
            if (backup.isDirectory()) {
                deleteDirectory(backup);
            }
        }
        if(!backup.exists())
            backup.mkdirs();
        XgpLottery.log("正在备份！");
        copyFile(new File(fold,"config.yml"),new File(backup,"config.yml"));
        copyFile(new File(fold,"database.yml"),new File(backup,"database.yml"));
        copyFile(new File(fold,"Data"),new File(backup,"Data"));
        copyFile(new File(fold,"lang"),new File(backup,"lang"));
        copyFile(new File(fold,"Record"),new File(backup,"Record"));
    }

    private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    private static void copyFile(File source, File destination) {
        if (source.isDirectory()) {
            destination.mkdirs();
            File[] files = source.listFiles();
            if (files != null) {
                for (File file : files) {
                    File destinationFile = new File(destination, file.getName());
                    copyFile(file, destinationFile);
                    // 递归
                }
            }
        } else {
            // 复制文件
            try {
                Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                XgpLottery.warning("error in backup data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
