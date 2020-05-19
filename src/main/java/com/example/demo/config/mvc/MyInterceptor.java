package com.example.demo.config.mvc;

import com.example.demo.core.ParseAnno;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Manseritis
 * @date 2019/12/30 15:09
 */
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 设置跨域
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept,token");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setCharacterEncoding("UTF-8");
        String token = null;
        // 下载文件
        String requestURI = request.getRequestURI();
        token = request.getHeader("token");
        if("/api/download".equals(requestURI)) token = request.getParameter("token");
        if(token==null){
            response.getWriter().write("{\"errorMsg\":\"token不能为空\"}");
            return false;
        }

        Object user =  request.getSession().getAttribute(token);
        if(user==null){
            response.getWriter().write("{\"errorMsg\":\"token失效\"}");
            return false;
        }

        boolean b = ParseAnno.checkPermissions(response);

        return b;
    }


}
