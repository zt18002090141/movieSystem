package com.system.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.common.Result;
import com.system.common.lang.Const;
import com.system.entity.Role;
import com.system.entity.User;
import com.system.entity.UserRole;
import com.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.system.common.BaseController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2022-11-29
 */
@RestController
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result list(String username){
        Page<User> userPage=userService.page(getPage(),new QueryWrapper<User>().like(StrUtil.isNotBlank(username),"username",username));
        userPage.getRecords().forEach(user->{
            user.setRoles(roleService.listRolesByUserId(user.getId()));
        });
        return Result.success(userPage);
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result info(@PathVariable Long id){
        User user =userService.getById(id);
        List<Role> roles=roleService.listRolesByUserId(user.getId());
        user.setRoles(roles);
        return Result.success(user);
    }
//保存用户
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result save(@RequestBody User user){
        //设置保存新用户的时间为当前时间
        user.setCreated(LocalDateTime.now());
        user.setPassword(bCryptPasswordEncoder.encode(Const.DEFAULT_PWD)); //设置初始的密码
        user.setAvatar(Const.DEFAULT_AVATAR); //设置新建用户的状态
        user.setStatu(Const.STATUS_ON);
        userService.save(user);
        return Result.success(user);
    }

//更新用户
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result update(@RequestBody User user){
        //更新用户的更新时间为当前时间
        user.setUpdated(LocalDateTime.now());
        userService.updateById(user);
        return Result.success(user);
    }
//    删除用户
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result del(@RequestBody Long[] ids){
        userService.removeByIds(Arrays.asList(ids));
        //同步相关数据 : 从Sys_user_role 关联表去 与该用户相关的数据
        userRoleService.remove(new QueryWrapper<UserRole>().in("user_id",ids));
        return Result.success("");
    }

//    分配角色
    @PostMapping("/role/{userId}")
    @PreAuthorize("hasAuthority('sys:user:role')")
    public Result rolePerm(@PathVariable Long userId,@RequestBody Long[] roleIds){
        //创建一个 存储 sys user role关联表的数据对象集合
        List<UserRole> userRoleList=new ArrayList<>();
        Arrays.stream(roleIds).forEach(roleId->{
            UserRole userRole=new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            userRoleList.add(userRole);
        });
//从sys user role关联表中先清空与该用户相关的所有的角色
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id",userId));
        //在按照上面userRoleList 重新录入进去
        userRoleService.saveBatch(userRoleList);
//删除缓存
        User user=userService.getById(userId);  //通过userId获得用户对象。
        userService.clearUserAuthorityInfo(user.getUsername()); //按照用户名清空权限缓存
        return Result.success("");
    }

//    重置密码
    @PostMapping("/repass/{userId}")
    public Result repass(@PathVariable Long userId){
        User user=userService.getById(userId);
        user.setPassword(bCryptPasswordEncoder.encode(Const.DEFAULT_PWD));
        user.setUpdated(LocalDateTime.now());
        userService.updateById(user);
        return Result.success("");
    }


}
