package com.example.demo.core;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manseritis
 * @date 2020/5/5 16:59
 */
public class ParseAnno {

    public static boolean checkPermissions(HttpServletResponse resp) throws ClassNotFoundException, IOException {
        String demoPath = System.getProperty("user.dir")+"\\src\\main\\java\\com\\example\\demo\\controller";
        File file = new File(demoPath);
        List<String> files = getFile(file, new ArrayList<>());
        for (String s : files) {
            Class<?> aClass = Class.forName(s);
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(CustomPermissions.class)){
                    String str = "";
                    if(method.isAnnotationPresent(PutMapping.class)){
                        PutMapping annotation = method.getAnnotation(PutMapping.class);
                        str = annotation.value()[0];
                    }else if(method.isAnnotationPresent(GetMapping.class)){
                        GetMapping annotation = method.getAnnotation(GetMapping.class);
                        str = annotation.value()[0];
                    }else if(method.isAnnotationPresent(PostMapping.class)){
                        PostMapping annotation = method.getAnnotation(PostMapping.class);
                        str = annotation.value()[0];
                    }else if(method.isAnnotationPresent(RequestMapping.class)){
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        str = annotation.value()[0];
                    }
                    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                    String requestURI = request.getRequestURI();

                    if(!requestURI.equals(str)) continue;
                    // 有该注解
                    CustomPermissions annotation = method.getAnnotation(CustomPermissions.class);
                    String value = annotation.value();
                    Subject subject = SecurityUtils.getSubject();
                    boolean permitted = subject.isPermitted(value);
                    if (!permitted){
                        // 没有该权限
                       resp.getWriter().write("{\"msg\":\"没有访问权限,请联系管理员\"}");
                       return false;
                    }
                }
            }
        }
        return true;
    }


    private static List<String> getFile(File file, List<String> result){
        String[] list = file.list();
        for (String str : list) {
            File file1 = new File(file + "\\"+str);
            if(file1.isFile()){
                if(file1.toString().contains(".java")){
                    String path = file1.toString();
                    path = path.replaceAll("\\\\","\\.").substring(path.indexOf("com"),path.indexOf(".java"));
                    result.add(path);
                }
            }else {
                getFile(new File(file +"\\"+ str), result);
            }
        }
        return result;
    }

}
