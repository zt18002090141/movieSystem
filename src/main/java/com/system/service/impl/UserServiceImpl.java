package com.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.entity.Menu;
import com.system.entity.Role;
import com.system.entity.User;
import com.system.mapper.UserMapper;
import com.system.service.MenuService;
import com.system.service.RoleService;
import com.system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.system.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Byterain
 * @since 2022-11-29
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    MenuService menuService;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public String getUserAuthorityInfo(Long userId) {
//      存储权限列表字符串
        String authority="";
        User user = userMapper.selectById(userId);
        if(redisUtil.hasKey(user.getUsername())){
//            若key存在，从缓存中读取
            authority=(String) redisUtil.get(user.getUsername());
        }else{
            List<Role> roles = roleService.listRolesByUserId(userId);
            if(roles.size() > 0){
                String roleCodes = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
                log.info("角色权限列表" + roleCodes);
            }
            List<Long> menuId = userMapper.getNavMenuIds(userId);
            if (menuId.size() > 0){
                List<Menu> menuLists = menuService.listByIds(menuId);
                String menuPerms = menuLists.stream().map(m->m.getPerms()).collect(Collectors.joining(","));
                authority = authority.concat(menuPerms);
                log.info("result:"+authority);
            }
        }

//        缓存

        redisUtil.set(user.getUsername(),authority);
        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String userName) {
        redisUtil.del(userName);
    }

    @Override
    public User getByUsername(String userName) {
        return this.getOne(new QueryWrapper<User>().eq("username",userName));

    }
    //根据菜单编号，找到与该菜单关联的用户名，通过用户名(就是redis中key)清空相关用户的权限缓存
    @Override
    public void clearUserAuthorityInfoMenuId(Long menuId) {
        List<User> users=userMapper.getUserListByMenuId(menuId); //查询与该菜单关联的用户(赋值了该菜单权限的用户)。
        users.forEach(u->{
            this.clearUserAuthorityInfo(u.getUsername()); //循环依大按照username用户名 redis存储权限key) ,清空redis中的权限缓
        });
    }
//角色管理:如果角色删除，根据删除的角色编号RoleId，查找赋值了该角色的用户，并且清除这些权限缓存。
    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        //查找和角色编号RoleId相关联的用户信息。
        List<User> users = this.list(new QueryWrapper<User>().inSql("id","select user_id from sys_user_role where role_id="+roleId));
        users.forEach(u->{
            this.clearUserAuthorityInfo(u.getUsername());  //循环次按照username用户名 (redis存储权限key) ，清空redis中的权限缓布
        });
    }
}
