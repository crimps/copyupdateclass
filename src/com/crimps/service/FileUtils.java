package com.crimps.service;

import sun.plugin2.util.SystemUtil;

import java.io.*;

/**
 * @author crimps
 * @create 2018-01-18 11:09
 **/
public class FileUtils {

    /**
     * 读取txt文件第一行
     * @param fileName 文件
     * @return
     * @throws Exception
     */
    public static String readTxtFileFirstLine(File fileName) throws Exception {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName));
        BufferedReader br = new BufferedReader(reader);
        return br.readLine();
    }

    /**
     * 将内容写入txt文件
     * @param content 内容
     * @param fileName 文件
     * @return
     * @throws Exception
     */
    public static boolean writeTxtFile(String content,File fileName)throws Exception{
        RandomAccessFile mm=null;
        boolean flag=false;
        FileOutputStream o=null;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes("GBK"));
            o.close();
            flag=true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            if(mm!=null){
                mm.close();
            }
        }
        return flag;
    }

    public static void open_directory(String folder) {
        File file = new File(folder);
        if (!file.exists()) {
            return;
        }
        Runtime runtime = null;
        try {
            runtime = Runtime.getRuntime();
            runtime.exec("cmd /c start explorer " + folder);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (null != runtime) {
                runtime.runFinalization();
            }
        }
    }
}
