package com.example.demo.config.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author Manseritis
 * @date 2020/4/30 17:26
 */
@Data
public class ResponseDTO<T> {
    private String code;
    private T data;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorMsg;

    public static <T> ResponseDTO success(T t){
        ResponseDTO response = new ResponseDTO();
        response.setCode("200");
        response.setData(t);
        return response;
    }

    public static ResponseDTO error(String code, String msg){
        ResponseDTO response = new ResponseDTO();
        response.setCode(code);
        response.setData(null);
        response.setErrorMsg(msg);
        return response;
    }
}
