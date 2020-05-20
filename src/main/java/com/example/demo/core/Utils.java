package com.example.demo.core;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author Manseritis
 * @date 2020/5/19 14:20
 */
public class Utils {
    /**
     * 获取项目根路径
     * @return
     */
    public static List<String> getFiles() throws IOException {
        String relativePath = getRelativePath();
        List<String> list = new ArrayList<>();
        String path = "";
        if(relativePath.contains(".jar!")){
            // 打包后的路径
            path = relativePath.substring(relativePath.indexOf("file:")+5, relativePath.indexOf(".jar!")+4);
            FileInputStream in = new FileInputStream(path);
            JarInputStream jarIn = new JarInputStream(in);
            JarEntry nextJarEntry = null;
            while ((nextJarEntry = jarIn.getNextJarEntry() )!= null){
                String name = nextJarEntry.getName();
                if(name.contains("controller")){
                    if(name.contains(".class")){
                        list.add(name.substring(name.indexOf("/com")+1,name.indexOf(".class")).replaceAll("/","."));
                    }
                }
            }
        }else {
            // 本地运行的路径
            path = relativePath + ("\\com\\example\\demo\\controller");
            ParseAnno.getFile(new File(path.replaceAll("file:/","")),list);
        }
        return list;
    }

    public static HttpServletRequest getRequest(){
        return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }


    private static String getRelativePath(){
        URL url = Utils.class.getClassLoader().getResource("");
        return url.toString();
    }

    public static String getCurrentPath(){
        String relativePath = getRelativePath();
        String jarPath = "";
        if(relativePath.contains(".jar")){
            String[] split = relativePath.split("/");
            for (int i = 1; i < split.length; i++) {
                if(!split[i].contains(".jar")){
                    jarPath += split[i] + "/";
                }else {
                    break;
                }
            }
        }else {
            jarPath = relativePath.replaceAll("file:","");
        }
        return File.separator + jarPath;
    }
}
