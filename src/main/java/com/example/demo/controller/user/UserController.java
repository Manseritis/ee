package com.example.demo.controller.user;

import com.example.demo.config.exception.ResponseDTO;
import com.example.demo.model.domain.SystemUser;
import com.example.demo.model.request.DirectoryRequest;
import com.example.demo.model.request.FileRequest;
import com.example.demo.model.request.UserRequest;
import com.example.demo.model.response.LoginResponse;
import com.example.demo.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Manseritis
 * @date 2020/2/21 17:59
 */
@RestController
@Slf4j
@RequestMapping("/api")
@CrossOrigin(allowCredentials="true")
public class UserController {
    @Resource
    UserService service;

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public ResponseDTO<List<SystemUser>> login(@RequestBody @Validated UserRequest reqUser, HttpServletRequest req){
        //添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken uToken = new UsernamePasswordToken(reqUser.getUsername(), reqUser.getPassword());
        // 认证
        subject.login(uToken);
        // 认证成功
        String uuid = UUID.randomUUID().toString();
        SystemUser user = (SystemUser) subject.getPrincipals().getPrimaryPrincipal();
        req.getSession().setAttribute("System_user_:"+user.getId(), uuid);
        req.getSession().setAttribute(uuid, user);
        return ResponseDTO.success(new LoginResponse(uuid,user.getId()));
    }


    @PostMapping("/add-directory")
    @ApiOperation(value = "新建文件夹")
    public ResponseDTO addDirectory(@RequestBody @Validated DirectoryRequest req,HttpServletRequest request){
        String msg = service.addDirectory(req);
        return msg==null?ResponseDTO.success("yes"):ResponseDTO.error("500",msg);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册")
    public ResponseDTO register(@RequestBody @Validated UserRequest reqUser){
        String msg = service.userRegister(reqUser);
        return msg==null?ResponseDTO.success("yes"):ResponseDTO.error("500",msg);
    }


    @GetMapping("/get-directory")
    @ApiOperation(value = "获取文件夹信息")
    public ResponseDTO<List<Map<String,Object>>> getDirectory(){
        List<Map<String,Object>> directory = service.getDirectory();
        return ResponseDTO.success(directory);
    }


    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    public ResponseDTO upload(FileRequest fileReq) throws IOException {
        String upload = service.upload(fileReq);
        return ResponseDTO.success("上传成功");
    }
}
