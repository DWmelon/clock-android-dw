package com.timediffproject.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import anet.channel.util.StringUtils;

/**
 * Created by melon on 2017/1/4.
 */

public class FileUtil {


    public static void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            FileChannel in = fi.getChannel();//得到对应的文件通道
            FileChannel out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fo != null) fo.close();
                if (fi != null) fi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static String formatFileSizeToString(long fileLen) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileLen < 1024) {
            fileSizeString = df.format((double) fileLen) + "B";
        } else if (fileLen < 1048576) {
            fileSizeString = df.format((double) fileLen / 1024) + "K";
        } else if (fileLen < 1073741824) {
            fileSizeString = df.format((double) fileLen / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileLen / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 根据路径删除
     */
    public static boolean deleteFile(File file) {
        return file != null && file.exists() && file.delete();
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return 返回文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 保存对象到文件
     *
     * @param obj
     * @param path
     */
    public static void writeObjectToPath(Object obj, String path) {
        if (TextUtils.isEmpty(path) || obj == null) {
            return;
        }


        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            File target = new File(path);
            if (!target.exists()) {
                target.createNewFile();
            }
            fos = new FileOutputStream(target);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    /**
     * 从path读取对象
     *
     * @param path
     * @return
     */
    public static Object readObjectFromPath(String path) {

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String readStringFromInputStream(InputStream in) {
        if (in == null) {
            return null;
        }

        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String str;

        try {
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                isr.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return sb.toString();


    }

    public static boolean copyFile(File srcFile, File destFile) {
        if (srcFile != null && srcFile.exists() && destFile != null) {
            return srcFile.renameTo(destFile);
        } else {
            return false;
        }
    }

    public static boolean isExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        return new File(path).exists();


    }


    public static List<String> searchFile(String path) {

        List<String> allPath = new ArrayList<>();
        File[] files = new File(path).listFiles();
        for (File file : files) {
            allPath.add(file.getPath());
        }

        return allPath;
    }

    public static boolean writeStreamToFile(InputStream inStream,String newPath){
        if (StringUtils.isBlank(newPath)){
            return false;
        }

        try{
            File file = new File(newPath);
            if (!file.exists()){
                file.createNewFile();
            }
            long byteSum = 0;
            int byteRead = 0;
            Log.d("oss", "file io start");
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];
            while ( (byteRead = inStream.read(buffer)) != -1) {
                byteSum += byteRead;
                fs.write(buffer, 0, byteRead);
            }
            inStream.close();

            Log.d("oss", "file io success");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("oss", "file io fail");
        }
        return false;
    }


}
