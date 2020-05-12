package com.example.demo.model.domain;

import lombok.Data;

/**
 * @author Manseritis
 * @date 2020/5/7 17:43
 */
@Data
public class UserDirectorys {
    Integer id;
    Integer parentId;
    String fileName;
    Integer UserId;
    Integer isDirectory;
    String updateTime;
}
