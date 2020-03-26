package com.hzl.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @RequestMapping(value = "/login-success",produces = {"text/plain;charset=UTF-8"})
    public String loginSuccess(){
        return getUserName()+"登录成功";
    }

    @GetMapping(value = "/logout",produces = {"text/plain;charset=UTF-8"})
    public String logout(HttpSession session){
        session.invalidate();
        return getUserName()+"退出成功";
    }

    @GetMapping(value = "/r/r1",produces = {"text/plain;charset=UTF-8"})
    @PreAuthorize("hasAuthority('/r/r1')")
    public String r1(){
        return getUserName()+"访问资源1";
    }

    @GetMapping(value = "/r/r2",produces = {"text/plain;charset=UTF-8"})
    @PreAuthorize("hasAuthority('/r/r2')")
    public String r2(){
        return getUserName()+"访问资源2";
    }

    //获取当前用户信息
    private String getUserName(){
        String username = null;
        //当前认证通过的用户身份
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //用户身份
        Object object = authentication.getPrincipal();
        if(object == null){
            username = "匿名";
        }
        if(object instanceof UserDetails){
            UserDetails userDetails = (UserDetails) object;
            username = userDetails.getUsername();
        }else {
            username = object.toString();
        }
        return username;
    }
}
