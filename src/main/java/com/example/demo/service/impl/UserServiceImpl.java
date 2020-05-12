package com.example.demo.service.impl;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.domain.SystemUser;
import com.example.demo.model.domain.UserDirectorys;
import com.example.demo.model.request.DirectoryRequest;
import com.example.demo.model.request.FileRequest;
import com.example.demo.model.request.UserRequest;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.tomcat.jni.Global;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Manseritis
 * @date 2020/4/30 15:42
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper mapper;


    @Override
    public SystemUser getSystemUserByName(String name) {
        return mapper.getUserByName(name);
    }

    @Override
    public String addDirectory(DirectoryRequest req) {
        String parentDircetory = getParentDircetory(req.getParentId());
        File addFile = new File(parentDircetory+req.getDirectoryName());
        if(!addFile.exists()){
            boolean mkdir = addFile.mkdir();
            if(!mkdir)  return "新建失败";
        }else{
            return "新建失败";
        }
        req.setUserId(getUser().getId());
        mapper.addDirectory(req);
        return null;
    }

    @Override
    public String userRegister(UserRequest req) {
        mapper.userRegister(req);
        String path = Global.class.getResource("/").getPath()+"/cloud_driver";
        String userName = req.getUsername();
        path = path +"/"+userName;
        // 得到用户根目录
        File file = new File(path);
        if(!file.exists()){
            // 用户根目录不存在 异常情况
            file.mkdir();
            SystemUser user = mapper.getUserByName(userName);
            if(user==null) return "注册失败";
            mapper.addDirectory(new DirectoryRequest(userName,0,user.getId(),1));
        }
        return null;
    }

    @Override
    public List<Map<String,Object>> getDirectory() {
        SystemUser user = (SystemUser) SecurityUtils.getSubject().getPrincipal();
        return mapper.getDirectory(user.getId());
    }

    @Override
    public String upload(FileRequest fileReq) throws IOException {
        String parentDircetory = getParentDircetory(fileReq.getParentId());
        MultipartFile file = fileReq.getFile();
        String fileName = file.getOriginalFilename();
        fileReq.getFile().transferTo(new File(parentDircetory+fileName));
        mapper.addDirectory(new DirectoryRequest(fileName,fileReq.getParentId(),getUser().getId(),0));
        return null;
    }









    private String getParentDircetory(Integer parentId1){
        SystemUser user = getUser();
        String path = Global.class.getResource("/").getPath()+"/cloud_driver";
        String userName = user.getUserName();
        path = path +"/"+userName;
        // 得到用户根目录
        String str = "/";
        boolean flag = true;
        Integer parentId =  parentId1;
        // 确定新建文件夹的路径
        while (flag){
            UserDirectorys directorys = mapper.findParentById(parentId);
            if(directorys==null){
                return "父级id错误";
            }
            if ( directorys.getParentId() == 0 ){
                flag = false;
            }else {
                // 未找到顶级继续执行
                str = directorys.getFileName() + "/" + str;
                parentId = directorys.getParentId();
            }
        }
        return path +"/"+str;
    }

    private SystemUser getUser(){
        return (SystemUser) SecurityUtils.getSubject().getPrincipal();
    }
}

