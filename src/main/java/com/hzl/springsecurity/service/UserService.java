package com.hzl.springsecurity.service;

import com.hzl.springsecurity.mapper.UserMapper;
import com.hzl.springsecurity.model.Permission;
import com.hzl.springsecurity.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        UserDto user = userMapper.getUserByUserName(username);
        if(user == null){
            //如果用户查询不到，返回null，由provider来抛异常
            return null;
        }
        //根据用户的id查询用户的权限
        List<Permission> permissionList = userMapper.findPermissionByUserId(user.getId());
        List<String> permissions = new ArrayList<>();
        permissionList.forEach(c -> permissions.add(c.getUrl()));
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);
        UserDetails userDetails = User.withUsername(user.getUsername()).password(user.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }
}
