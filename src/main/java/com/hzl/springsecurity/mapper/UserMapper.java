package com.hzl.springsecurity.mapper;

import com.hzl.springsecurity.model.Permission;
import com.hzl.springsecurity.model.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    UserDto getUserByUserName(@Param("username")String username);

    List<Permission> findPermissionByUserId(@Param("id")String id);
}
