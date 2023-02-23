package com.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.common.Result;
import com.system.entity.Menu;
import com.system.entity.RoleMenu;
import com.system.service.MenuService;
import com.system.service.RoleMenuService;
import com.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.system.common.BaseController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2022-11-29
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    RoleMenuService roleMenuService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result getMenuList(){
      List<Menu> menuList= menuService.tableTree();
        return  Result.success(menuList);
    }
//根据id获取对应菜单信息
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result info(@PathVariable Long id){
        return Result.success(menuService.getById(id));
    }
//保存新菜单
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public Result save(@RequestBody Menu menu){
        menu.setCreated(LocalDateTime.now()); //设置添加时间
        menuService.save(menu); //保存添加信息
        return Result.success(menu);
    }
    //跟新菜单
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public Result update(@RequestBody Menu menu){
        menu.setUpdated(LocalDateTime.now());
        menuService.updateById(menu);
        //清空与该菜单相关的权限缓存
        userService.clearUserAuthorityInfoMenuId(menu.getId());
        return Result.success(menu);
    }
//    删除菜单
    @PostMapping("/delete{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result del(@PathVariable Long id){
        //通过id查询删除的菜单,有多少个子菜单
        int count = menuService.count(new QueryWrapper<Menu>().eq("parent_id",id));
        if (count>0){
            return Result.fail("请先删除子菜单");
        }else {
            //清除与该菜单关联的权限缓存
            userService.clearUserAuthorityInfoMenuId(id);
            //删除菜单
            menuService.removeById(id);
            //如果菜单删除，中间表 sys_role_menu 数据同步删除。
            // 删除sys_role_menu中间关采表中的 相关联的数据
            roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("menu_id",id));
            return Result.success("");
        }
    }
}

