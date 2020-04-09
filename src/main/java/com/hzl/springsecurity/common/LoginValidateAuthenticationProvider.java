package com.hzl.springsecurity.common;

import com.hzl.springsecurity.mapper.UserMapper;
import com.hzl.springsecurity.model.UserDto;
import com.hzl.springsecurity.service.UserService;
import com.hzl.springsecurity.util.MD5;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginValidateAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取输入的用户名
        String username = authentication.getName();
        //获取输入的明文
        String rawPassword = (String) authentication.getCredentials();

        if (username == null || username.equals("")){
            throw new BadCredentialsException("用户名不能为空！");
        }

        if (rawPassword == null || rawPassword.equals("")){
            throw new BadCredentialsException("密码不能为空！");
        }

        //查询用户是否存在
        UserDetails user = userService.loadUserByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("用户不存在！");
        }

        UserDto userDto = userMapper.getUserByUserName(username);
//        if(user != null) {
            if (!user.isEnabled()) {
                throw new DisabledException("该账户已被禁用，请联系管理员");

            } else if (!user.isAccountNonLocked()) {
                throw new LockedException("该账号已被锁定");

            } else if (!user.isAccountNonExpired()) {
                throw new AccountExpiredException("该账号已过期，请联系管理员");

            } else if (!user.isCredentialsNonExpired()) {
                throw new CredentialsExpiredException("该账户的登录凭证已过期，请重新登录");
            }

            //验证密码
            if (!MD5.validPassword(rawPassword, user.getPassword(), userDto.getSalt())) {
                throw new BadCredentialsException("输入密码错误!");
            }

            return new UsernamePasswordAuthenticationToken(user, rawPassword, user.getAuthorities());
//        }
//        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
