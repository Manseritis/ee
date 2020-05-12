package com.example.demo.config.mvc;

import com.example.demo.config.exception.ResponseDTO;
import com.example.demo.core.ParseAnno;
import org.apache.shiro.subject.Subject;
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
        String token = request.getHeader("token");
        response.setContentType("application/json;charset=utf-8");
        if(token==null){
            response.getWriter().write("{\"msg\":\"token不能为空\"}");
            return false;
        }

        Subject subject = (Subject) request.getSession().getAttribute(token);
        if(subject==null){
            response.getWriter().write("{\"msg\":\"token失效\"}");
            return false;
        }

        boolean b = ParseAnno.checkPermissions(response);

        return b;
    }


}
