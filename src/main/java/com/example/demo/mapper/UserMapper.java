package com.example.demo.mapper;

import com.example.demo.model.domain.SystemUser;
import com.example.demo.model.domain.UserDirectorys;
import com.example.demo.model.request.DirectoryRequest;
import com.example.demo.model.request.UserRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.tomcat.jni.Directory;

import java.util.List;
import java.util.Map;

/**
 * @author Manseritis
 * @date 2020/4/30 15:49
 */
@Mapper
public interface UserMapper {
    /**
     * 登录验证
     * @param name
     * @return
     */
    SystemUser getUserByName(String name);

    /**
     * 添加文件
     * @param req
     */
    void addDirectory(DirectoryRequest req);

    /**
     * 获取文件夹的父级文件夹
     * @param id
     * @return
     */
    UserDirectorys findParentById(Integer id);
    /**
     * 注册
     * @param req
     * @return
     */
    Integer userRegister(UserRequest req);

    /**
     * 获取用户的文件夹信息
     * @return
     */
    List<Map<String,Object>> getDirectory(Integer id);

    List<Map> getChildByParentId(Integer parentId);

    void deleteFiles(List<Map> list);
}
