package com.joey.general.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Joey on 2016/6/12.
 */
public class ZipUtil {

    public static boolean compress(String zipFileName, String filePath) {
        File inputFile = new File(filePath);
        if (!inputFile.exists())
            return false;
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipFileName));
            BufferedOutputStream bo = new BufferedOutputStream(out);
            compress(out, inputFile, inputFile.getName(), bo);
            bo.close();
            out.close(); // 输出流关闭
        } catch (IOException exception) {
            return false;
        }
        catch (Exception exception) {
            return false;
        }
        return true;
    }

    private static void compress(ZipOutputStream out, File f, String base,
                            BufferedOutputStream bo) throws Exception { // 方法重载
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            if (fl.length == 0) {
                out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
                System.out.println(base + "/");
            }
            for (int i = 0; i < fl.length; i++) {
                compress(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
            }

        } else {
            out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
            System.out.println(base);
            FileInputStream in = new FileInputStream(f);
            BufferedInputStream bi = new BufferedInputStream(in);
            int b;
            while ((b = bi.read()) != -1) {
                bo.write(b); // 将字节流写入当前zip目录
            }
            bi.close();
            in.close(); // 输入流关闭
        }
    }

    public static boolean deCompress(String zipFileName, String sourceDir) {
        try {
            ZipInputStream Zin=new ZipInputStream(new FileInputStream(
                    zipFileName));//输入源zip路径
            BufferedInputStream Bin=new BufferedInputStream(Zin);
            File Fout=null;
            ZipEntry entry;
            try {
                while((entry = Zin.getNextEntry())!=null && !entry.isDirectory()){
                    Fout=new File(sourceDir,entry.getName());
                    if(!Fout.exists()){
                        (new File(Fout.getParent())).mkdirs();
                    }
                    FileOutputStream out=new FileOutputStream(Fout);
                    BufferedOutputStream Bout=new BufferedOutputStream(out);
                    int b;
                    while((b=Bin.read())!=-1){
                        Bout.write(b);
                    }
                    Bout.close();
                    out.close();
                    System.out.println(Fout+"解压成功");
                }
                Bin.close();
                Zin.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
