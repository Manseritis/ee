package com.example.demo.service;

import com.example.demo.model.domain.SystemUser;
import com.example.demo.model.request.DirectoryRequest;
import com.example.demo.model.request.FileRequest;
import com.example.demo.model.request.UserRequest;
import io.swagger.models.auth.In;
import org.apache.tomcat.jni.Directory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Manseritis
 * @date 2020/4/30 15:33
 */
public interface UserService {
    /**
     * 登录验证
     * @param name
     * @return
     */
    SystemUser getSystemUserByName(String name);

    /**
     * 添加文件夹
     */
    String addDirectory(DirectoryRequest req);

    /**
     * 注册
     * @param req
     * @return
     */
    String userRegister(UserRequest req);

    /**
     * 获取用户的文件夹信息
     * @return
     */
    List<Map<String,Object>> getDirectory();


    String upload(FileRequest fileReq) throws IOException;
}
