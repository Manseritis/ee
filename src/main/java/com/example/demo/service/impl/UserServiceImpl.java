package com.example.demo.service.impl;

import com.example.demo.core.Utils;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.domain.SystemUser;
import com.example.demo.model.domain.UserDirectorys;
import com.example.demo.model.request.DirectoryRequest;
import com.example.demo.model.request.FileRequest;
import com.example.demo.model.request.IdRequest;
import com.example.demo.model.request.UserRequest;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.tomcat.jni.Global;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
        log.info("新增文件getParentDircetory():" + parentDircetory);
        File addFile = new File(parentDircetory+req.getDirectoryName());
        if(!addFile.exists()){
            boolean mkdir = addFile.mkdir();
            if(!mkdir)  return "创建文件失败";
        }else{
            return "文件夹已存在";
        }
        req.setUserId(getUser().getId());
        try{
            mapper.addDirectory(req);
        }catch (Exception e){
            addFile.delete();
            return "写入数据库时失败,失败原因为:" + e.getMessage();
        }
        return null;
    }

    @Override
    public String userRegister(UserRequest req) {
        mapper.userRegister(req);
        String path = Utils.getCurrentPath();
        path = path +"cloud_driver" + File.separator;
        String userName = req.getUsername();
        path = path + userName;
        // 得到用户根目录
        File file = new File(path);
        if(!file.exists()){
            // 用户根目录不存在 异常情况
            boolean mkdir = file.mkdirs();
            if(!mkdir) return "创建用户目录失败";
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
    public String upload(FileRequest fileReq){
        if(fileReq.getParentId()==0){
            return "根路径下不可上传文件";
        }
        String parentDircetory = getParentDircetory(fileReq.getParentId());
        log.info("上传文件的地址:"+ parentDircetory + fileReq.getFile().getOriginalFilename());
        MultipartFile file = fileReq.getFile();
        String fileName = file.getOriginalFilename();
        try {
            File file1 = new File(parentDircetory + fileName);
            if(file1.exists()){
                throw new IOException("文件夹已存在");
            }
            file.transferTo(file1);
        } catch (IOException e) {
            return "上传文件时失败,失败原因为:" + e.getMessage();
        }
        try{
            mapper.addDirectory(new DirectoryRequest(fileName,fileReq.getParentId(),getUser().getId(),0));
        }catch (Exception e){
            return "上写入数据库时失败,失败原因为:" + e.getMessage();
        }
        return null;
    }

    @Override
    public String delete(IdRequest req) {
        // 获取目标路径
        String parentDircetory = getParentDircetory(req.getId());
        File file = new File(parentDircetory);
        if(file.exists()){
            boolean delete = file.delete();
            if(!delete)
            return "删除失败";
        }else {
            return file.toString();
        }
        try {
            List list = new ArrayList();
            getChilds(req.getId(), list);
            HashMap<Object, Object> map = new HashMap<>();
            map.put("id",req.getId());
            list.add(map);
            mapper.deleteFiles(list);
        }catch (Exception e){
            return "删除时失败,可能是删除数据库数据时失败";
        }
        return null;
    }

    @Override
    public String download(IdRequest req, HttpServletResponse resp) {
        String file = getParentDircetory(req.getId());
        String[] split = file.split("/");
        FileInputStream in = null;
        ServletOutputStream outputStream = null;
        resp.setContentType("application/x-download");
        try {
            resp.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(split[split.length-1], "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            in = new FileInputStream(file);
            outputStream = resp.getOutputStream();
            byte[] b = new byte[1024];
            int i = 0;
            while ((i=in.read(b))>0){
                outputStream.write(b,0,i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private void getChilds(Integer parentId, List<Map> list){
        List<Map> resList = mapper.getChildByParentId(parentId);
        // 还有子节点
        Map map1 = null;
        if(resList.size()>0){
            for (int i = 0; i < resList.size(); i++) {
                Map map = resList.get(i);
                map1 = new HashMap();
                Integer id = (Integer) map.get("id");
                map1.put("id",id);
                list.add(map1);
                getChilds(id, list);
            }
        }
    }

    private String getParentDircetory(Integer parentId1){
        SystemUser user = getUser();
        String path = Utils.getCurrentPath() + "cloud_driver" + File.separator;
        String userName = user.getUserName();
        path = path + userName;
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
        return path + File.separator + str ;
    }

    private SystemUser getUser(){
        return (SystemUser) SecurityUtils.getSubject().getPrincipal();
    }
}

