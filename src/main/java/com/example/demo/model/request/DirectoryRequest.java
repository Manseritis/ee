package com.example.demo.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * @author Manseritis
 * @date 2020/5/7 17:06
 */
@Data
@AllArgsConstructor
public class DirectoryRequest {
    @NotNull
    String directoryName;
    @NotNull
    @DecimalMin(message = "parentId不能为0", value = "1")
    Integer parentId;
    @NotNull
    Integer userId;
    @NotNull
    Integer isDirectory;
}
