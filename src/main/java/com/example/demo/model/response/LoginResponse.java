package com.example.demo.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Manseritis
 * @date 2020/5/5 13:50
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    String token;
    Integer userId;
}
