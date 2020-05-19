package com.example.demo.config.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Manseritis
 * @date 2019/12/26 10:36
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").addResourceLocations("classpath:/templates/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/api/*")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/api/register")
                .excludePathPatterns("/*.*");
    }
}
