package com.hzl.springsecurity.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LindTokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (!httpServletRequest.getRequestURI().equals("/spring-security/login")){

        String token = httpServletRequest.getHeader("Authorization");
        if (token == null){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", "400");//400
            map.put("msg", "请先登录获取token");
            httpServletResponse.setContentType("UTF-8");
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(httpServletResponse.getOutputStream(), map);
            } catch (Exception a) {
                throw new ServletException();
            }
        }
        else {
            if (!token.equals("1")){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", "400");//400
                map.put("msg", "无效token");
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(httpServletResponse.getOutputStream(), map);
                } catch (Exception a) {
                    throw new ServletException();
                }
            }
        }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
