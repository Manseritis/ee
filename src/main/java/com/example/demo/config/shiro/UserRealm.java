package com.example.demo.config.shiro;

import com.example.demo.model.domain.SystemUser;
import com.example.demo.service.UserService;
import org.apache.catalina.Server;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Manseritis
 * @date 2020/3/9 17:17
 */
public class UserRealm extends AuthorizingRealm {
    @Resource
    UserService service;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermission("/add");
        info.addStringPermission("/all");
        info.addStringPermission("/update");
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if(authenticationToken.getPrincipal()==null){
            return null;
        }
        //获取用户的登录信息，用户名
        String phone=authenticationToken.getPrincipal().toString();
        SystemUser systemUserByName = service.getSystemUserByName(phone);
        if(systemUserByName==null){
            return null;
        }else {
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            request.getSession().setAttribute("user",systemUserByName);
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(systemUserByName,systemUserByName.getPassword(),getName());
            return info;
        }
    }
}
