package com.crimps.service;

import org.apache.commons.io.FileUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author crimps
 * @create 2018-01-09 17:21
 **/
public class CopyUtils {

    private static final String WEB_FILE = "/src/main/webapp";
    private static final String CLASS_FILE = "/src/main/java";
    private static final String EX_CLASS_FILE = "/WEB-INF/classes";
    private static final String EN_JAVA_FILE = ".java";
    private static final String EN_CLASS_FILE = ".class";
    private static final String DIR_NAME = "scims-server";

    public static final String OLD_KEY = "OLD";
    public static final String NEW_KEY = "NEW";
    public static final String DIR = "DIR";

    public static Map<String, Object> copy(String javaContent, String classFilePath) {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> javaFileList = Arrays.asList(javaContent.split("\n"));
        ArrayList<String> filePathList = analyJavaContent(javaFileList);
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File desktop = fsv.getHomeDirectory();
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String descFile_ex = desktop.getPath() + File.separator + DIR_NAME + "_" + dateFormat.format(new Date());
        for (int i = 0; i < filePathList.size(); i++) {
            try {
                String sourceFile = classFilePath + File.separator + filePathList.get(i);
                String descFile =  descFile_ex + File.separator + DIR_NAME + File.separator + filePathList.get(i);
                System.out.println("##" + sourceFile + "##" + descFile);
                FileUtils.copyFile(new File(sourceFile), new File(descFile));
                javaFileList.set(i, javaFileList.get(i) + "复制成功");
            } catch (Exception e) {
                e.printStackTrace();
                javaFileList.set(i, javaFileList.get(i) + "复制失败");
            }

        }
        resultMap.put(DIR, descFile_ex);
        resultMap.put(OLD_KEY, javaFileList);
        resultMap.put(NEW_KEY, filePathList);
        return resultMap;
    }

    /**
     * 将svn的记录转换成编译后的路径
     * @param javaFileList
     * @return
     */
    private static ArrayList<String> analyJavaContent(List<String> javaFileList) {
        ArrayList<String> resultFileList = new ArrayList<>();
        for (String javaFile : javaFileList) {
            //区分class和页面文件
            if (javaFile.contains(WEB_FILE)) {
                System.out.println("#" + javaFile.indexOf(WEB_FILE));
                System.out.println(javaFile.substring(javaFile.indexOf(WEB_FILE)));
                resultFileList.add(javaFile.substring(javaFile.indexOf(WEB_FILE)).replace(WEB_FILE, ""));
            } else if (javaFile.contains(CLASS_FILE)) {
                System.out.println("$" + javaFile.indexOf(CLASS_FILE));
                resultFileList.add(EX_CLASS_FILE + javaFile.substring(javaFile.indexOf(CLASS_FILE)).
                        replace(CLASS_FILE, "").
                        replace(EN_JAVA_FILE, EN_CLASS_FILE));
            }
        }
        return resultFileList;
    }
}
