package com.hzl.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity //启用Spring Security
@EnableGlobalMethodSecurity(prePostEnabled = true) //会拦截注解了@PreAuthrize注解的配置
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() //屏蔽CSRF控制
                .authorizeRequests()
//                .antMatchers("/r/r1").hasAnyAuthority("/r/r1")//需要/r/r1权限，基于url授权，开启方法授权时不必使用
//                .antMatchers("/r/r2").hasAnyAuthority("/r/r2")//需要/r/r2权限，基于url授权，开启方法授权时不必使用
                .antMatchers("/r/**").authenticated()//所有的/r/**的请求必须认证通过
                .anyRequest().permitAll()//除了/r/**，其他的请求可以访问
                .and()
                .formLogin()//允许表单登录
                .loginPage("/login-view")//自定义登录页面
                .loginProcessingUrl("/login")
                .successForwardUrl("/login-success")//自定义登录成功的页面地址
                .and()
                .sessionManagement()
                //生成session，IF_REQUIRED（默认）登录时创建，ALWAYS如果没有session存在就创建一个，
                //NEVER，springsecurity将不会创建session，但是如果应用中其他地方创建了session，那么springsecurity将会使用它
                //STATELESS,springsecurity将绝对不会创建session，也不使用session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login-view?logout");//自定义退出登录成功的页面地址
    }

    @Bean //注入PasswordEncoder
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
