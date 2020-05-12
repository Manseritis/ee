package com.example.demo.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Manseritis
 * @date 2020/4/30 15:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemUser {
    Integer id;
    String userName;
    String password;
    String code;
}
