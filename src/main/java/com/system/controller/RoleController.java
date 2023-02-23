package com.system.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.common.Result;
import com.system.common.lang.Const;
import com.system.entity.Role;
import com.system.entity.RoleMenu;
import com.system.entity.UserRole;
import com.system.util.RedisUtil;
import freemarker.template.utility.StringUtil;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.system.common.BaseController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2022-11-29
 */
@RestController
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    RedisUtil redisUtil;
//    获得所有角色信息
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result list( String name){//name就是搜索栏中内容，有name就模糊查询，没有就全部出来
        //查询分页数据 page()，查询出的数据不是List而是Page<Role>
        //Page<Role>对象,就是带有分页数据的List ,查询的角色集合在records中。其它就是分页数据 :"total": 2,"size": 5,"current":1
        Page<Role> roles= roleService.page(getPage(),new QueryWrapper<Role>().like(StrUtil.isNotBlank(name),"name",name));
        return Result.success(roles);
    }

//    编辑角色
    @GetMapping("/info{id}")
    public Result info(@PathVariable Long id){
       Role role = roleService.getById(id);
       List<RoleMenu> roleMenus = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id",id));
       List<Long> menuIds= roleMenus.stream().map(rm-> rm.getMenuId()).collect(Collectors.toList());
       role.setMenuIds(menuIds);
       return Result.success(role);
    }
//    编辑角色：更新
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result update(@RequestBody Role role){
        role.setUpdated(LocalDateTime.now());
        roleService.updateById(role);
        return Result.success(role);
    }
// 新建角色
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result save(@RequestBody Role role){
        role.setCreated(LocalDateTime.now());
        role.setStatu(Const.STATUS_ON);
        roleService.save(role);
        return Result.success(role);
    }
//    删除
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @Transactional
    public Result del(@RequestBody Long[] roleIds){
        roleService.removeByIds(Arrays.asList(roleIds));
//        同步删除其他数据
        roleMenuService.remove(new QueryWrapper<RoleMenu>().in("role_id",roleIds));
        userRoleService.remove(new QueryWrapper<UserRole>().in("role_id",roleIds));
        return Result.success("");
    }
//    分配权限
    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:perm')")
    @Transactional
    public Result perm(@PathVariable Long roleId,@RequestBody Long[] menuIds){
        List<RoleMenu> roleMenuList =new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId->{
            RoleMenu new_rm=new RoleMenu();
            new_rm.setMenuId(menuId);
            new_rm.setRoleId(roleId);
            roleMenuList.add(new_rm);
        });

        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id",roleId));
        roleMenuService.saveBatch(roleMenuList);
        userService.clearUserAuthorityInfoByRoleId(roleId);

        return Result.success(menuIds);
    }
}
