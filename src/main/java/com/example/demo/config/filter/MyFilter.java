package com.example.demo.config.filter;

import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Manseritis
 * @date 2020/5/13 11:19
 */

@Component
@WebFilter(urlPatterns = {"/api/login","/api/register"})
public class MyFilter implements Filter {

        @Override
        public void destroy() {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response,
                             FilterChain chain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse rep = (HttpServletResponse) response;
            // 设置跨域
            rep.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
            rep.setHeader("Access-Control-Allow-Credentials","true");
            rep.setHeader("Access-Control-Allow-Headers",
                    "Origin, X-Requested-With, Content-Type, Accept,token");
            rep.setHeader("Access-Control-Max-Age", "3600");

            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        }

        @Override
        public void init(FilterConfig arg0) throws ServletException {

        }



}
