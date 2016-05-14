package com.joey.general.utils;

import android.os.Environment;
import android.os.StatFs;

import com.joey.expresscall.AppConsts;

import java.io.File;

public class MobileUtil {
    
    /**
     * 创建手机软件所需要的文件夹
     */
    public static void creatAllFolder() {
        MyLog.e("MobileUtil", "creatAllFolder");
        File rootFile = new File(AppConsts.APP_PATH);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }

        createDirectory(AppConsts.LOG_PATH);
        createDirectory(AppConsts.RECORD_DIR);
        createDirectory(AppConsts.FILE_DIR);

      
    }

    public static void reNameFile(String oldFileName, String newFileName) {
        File oleFile = new File(oldFileName); //要重命名的文件或文件夹
        File newFile = new File(newFileName);  //重命名为
        MyLog.e("rename", "oleFile=" + oleFile + "----newFile=" + newFile);
        oleFile.renameTo(newFile);  //执行重命名
    }

    /**
     * 递归创建文件目录
     *
     * @param filePath 要创建的目录路径
     * @author
     */
    public static void createDirectory(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        File parentFile = file.getParentFile();

        if (null != file && parentFile.exists()) {
            if (parentFile.isDirectory()) {
            } else {
                parentFile.delete();
                boolean res = parentFile.mkdir();
                if (!res) {
                    parentFile.delete();
                }
            }

            boolean res = file.mkdir();
            if (!res) {
                file.delete();
            }

        } else {
            createDirectory(file.getParentFile().getAbsolutePath());
            boolean res = file.mkdir();
            if (!res) {
                file.delete();
            }
        }
    }

    /**
     * 递归删除文件和文件夹,清空文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 获取剩余sdk卡空间
     *
     * @return
     */
    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }


}
