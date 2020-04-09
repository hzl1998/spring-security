package com.hzl.springsecurity.config;

import com.hzl.springsecurity.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //启用Spring Security
@EnableGlobalMethodSecurity(prePostEnabled = true) //会拦截注解了@PreAuthrize注解的配置
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthExceptionEntryPoint authExceptionEntryPoint;

    @Autowired
    HzlAuthenticationSuccessHandler authenticationSuccessHandler;  // 登录成功返回的 JSON 格式数据给前端（否则为 html）

    @Autowired
    HzlAuthenticationFailureHandler authenticationFailureHandler;  //  登录失败返回的 JSON 格式数据给前端（否则为 html）

    @Autowired
    HzlLogoutSuccessHandler logoutSuccessHandler;  // 注销成功返回的 JSON 格式数据给前端（否则为 登录时的 html）

    @Autowired
    HzlAccessDeniedHandler accessDeniedHandler;    // 无权访问返回的 JSON 格式数据给前端（否则为 403 html 页面）

    @Autowired
    private LoginValidateAuthenticationProvider loginValidateAuthenticationProvider;

    @Autowired
    LindTokenAuthenticationFilter lindTokenAuthenticationFilter;

    //安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() //屏蔽CSRF控制
                .httpBasic().authenticationEntryPoint(authExceptionEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/r/**").authenticated()//所有的/r/**的请求必须认证通过
                .anyRequest().permitAll()//除了/r/**，其他的请求可以访问
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll();
        http.logout().logoutSuccessHandler(logoutSuccessHandler);
        http.addFilterBefore(lindTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Bean //注入PasswordEncoder
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //自定义验证密码
        auth.authenticationProvider(loginValidateAuthenticationProvider);
    }
}
