package com.joey.general.utils;

import android.os.Environment;
import android.os.StatFs;

import com.joey.duduexpress.AppConsts;

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

//        if (AppConsts.DEBUG_STATE) {//调试版本
            AppConsts.LOG_PATH = AppConsts.LOG_PATH_SHOW;
            AppConsts.LOG_CLOUD_PATH = AppConsts.LOG_CLOUD_PATH_SHOW;
            AppConsts.LOG_ACCOUNT_PATH = AppConsts.LOG_ACCOUNT_PATH_SHOW;
//        } else {
//            AppConsts.LOG_PATH = AppConsts.LOG_PATH_DISMISS;
//            AppConsts.LOG_CLOUD_PATH = AppConsts.LOG_CLOUD_PATH_DISMISS;
//            AppConsts.LOG_ACCOUNT_PATH = AppConsts.LOG_ACCOUNT_PATH_DISMISS;
//        }

        createDirectory(AppConsts.LOG_PATH);
        createDirectory(AppConsts.LOG_CLOUD_PATH);
        createDirectory(AppConsts.LOG_ACCOUNT_PATH);

        createDirectory(AppConsts.CAPTURE_PATH);
        createDirectory(AppConsts.VIDEO_PATH);
        createDirectory(AppConsts.DOWNLOAD_VIDEO_PATH);
        createDirectory(AppConsts.DOWNLOAD_IMAGE_PATH);
        createDirectory(AppConsts.BBSIMG_PATH);
        createDirectory(AppConsts.BUG_PATH);
        createDirectory(AppConsts.HEAD_PATH);
        createDirectory(AppConsts.WELCOME_IMG_PATH);
        createDirectory(AppConsts.SCENE_PATH);
        createDirectory(AppConsts.CLOUDVIDEO_PATH);
        createDirectory(AppConsts.ALARM_IMG_PATH);
        createDirectory(AppConsts.ALARM_VIDEO_PATH);
        createDirectory(AppConsts.CAT_PATH);
    }


    public static void showLogFile(boolean show) {
        if (show) {//显示log文件
            reNameFile(AppConsts.LOG_PATH, AppConsts.LOG_PATH_SHOW);
            reNameFile(AppConsts.LOG_CLOUD_PATH, AppConsts.LOG_CLOUD_PATH_SHOW);
            reNameFile(AppConsts.LOG_ACCOUNT_PATH, AppConsts.LOG_ACCOUNT_PATH_SHOW);
            AppConsts.LOG_PATH = AppConsts.LOG_PATH_SHOW;
            AppConsts.LOG_CLOUD_PATH = AppConsts.LOG_CLOUD_PATH_SHOW;
            AppConsts.LOG_ACCOUNT_PATH = AppConsts.LOG_ACCOUNT_PATH_SHOW;
        } else {
            reNameFile(AppConsts.LOG_PATH, AppConsts.LOG_PATH_DISMISS);
            reNameFile(AppConsts.LOG_CLOUD_PATH, AppConsts.LOG_CLOUD_PATH_DISMISS);
            reNameFile(AppConsts.LOG_ACCOUNT_PATH, AppConsts.LOG_ACCOUNT_PATH_DISMISS);
            AppConsts.LOG_PATH = AppConsts.LOG_PATH_DISMISS;
            AppConsts.LOG_CLOUD_PATH = AppConsts.LOG_CLOUD_PATH_DISMISS;
            AppConsts.LOG_ACCOUNT_PATH = AppConsts.LOG_ACCOUNT_PATH_DISMISS;
        }

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
