package com.example.demo.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Manseritis
 * @date 2020/5/11 11:23
 */
@Data
public class FileRequest {
    Integer parentId;
    MultipartFile file;
}
