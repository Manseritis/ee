package com.example.demo.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Manseritis
 * @date 2020/5/5 13:45
 */
@Data
public class UserRequest {
    @NotNull(message = "用户名不能为空")
    String username;
    @NotNull(message = "密码不能为空")
    String password;
    String code;
}
