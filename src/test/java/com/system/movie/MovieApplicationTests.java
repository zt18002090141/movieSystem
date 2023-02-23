package com.system.movie;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.common.Result;
import com.system.entity.Role;
import com.system.entity.User;
import com.system.mapper.UserMapper;
import com.system.service.FilmService;
import com.system.service.RoleService;
import com.system.service.UserService;
import com.system.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
class MovieApplicationTests {

//    @Autowired
//    UserMapper userMapper;
//    @Autowired
//    JwtUtils jwtUtils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    FilmService filmService;

    @Test
    void contextLoads() {
//        User one = userService.getOne(new QueryWrapper<User>().eq("username","admin"));
//        System.out.println(one);
//
//        String pwd="1234";
//        String pass=bCryptPasswordEncoder.encode(pwd);
//        System.out.println("pass"+pass);
//        List<Role>  roles=roleService.list(new QueryWrapper<Role>().inSql("id","select role_id from sys_user_role where user_id="+1));
//        if(roles.size()>0){
//            String roleCodes=   roles.stream().map(r->"ROLE_"+r.getCode()).collect(Collectors.joining(","));
//            roleCodes=roleCodes.concat(",");
//            log.info("角色权限列表"+roleCodes);
//        }
      //  userService.getUserAuthorityInfo(Long.valueOf(1));

//System.out.println(filmService.getCategoryById(1L));


    }

}
