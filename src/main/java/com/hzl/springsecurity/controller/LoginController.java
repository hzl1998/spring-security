package com.hzl.springsecurity.controller;

import com.hzl.springsecurity.mapper.UserMapper;
import com.hzl.springsecurity.model.UserDto;
import com.hzl.springsecurity.service.UserService;
import com.hzl.springsecurity.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

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
