package com.example.demo.config.exception;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * @author Manseritis
 * @date 2020/4/30 16:58
 */
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(AuthenticationException .class)
    public ResponseDTO unKnowUser(AuthenticationException e){
        return ResponseDTO.error("500","用户名或密码未知");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDTO  validationException(MethodArgumentNotValidException e){
        return ResponseDTO.error("500",e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
