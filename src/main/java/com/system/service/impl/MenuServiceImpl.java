package com.system.service.impl;

import com.system.entity.Menu;
import com.system.entity.User;
import com.system.entity.dto.MenuDto;
import com.system.mapper.MenuMapper;
import com.system.mapper.UserMapper;
import com.system.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Byterain
 * @since 2022-11-29
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    MenuService menuService;
    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<MenuDto> getCurrentUserNav(String username) {

        User user = userService.getByUsername(username);
        List<Long> menuIds = userMapper.getNavMenuIds(user.getId());
        List<Menu> menus= this.listByIds(menuIds);
        List<Menu> new_menu=this.buildChildrenMenu(menus);
        return this.buildTreeMenu(new_menu);
    }

    @Override
    public List<Menu> tableTree() {
        List<Menu> menuList = this.list();
        return buildChildrenMenu(menuList);
    }

    public List<MenuDto> buildTreeMenu(List<Menu> new_menu){
        List<MenuDto> tree_menu=new ArrayList<>();
        new_menu.forEach(m->{
            MenuDto temp=new MenuDto();
            temp.setId(m.getId());
            temp.setName(m.getPerms());
            temp.setTitle(m.getName());
            temp.setComponent(m.getComponent());
            temp.setPath(m.getPath());
            temp.setIcon(m.getIcon());
            if(m.getChildren().size()>0){
                temp.setChildren(buildTreeMenu(m.getChildren()));
            }
            tree_menu.add(temp);
        });
        return tree_menu;
    }
    public List<Menu> buildChildrenMenu(List<Menu> menus){
        List<Menu> menuTree = new ArrayList<>();
        for(Menu menu:menus)  {
          for(Menu e:menus){
              if(menu.getId() == e.getParentId()){

                  menu.getChildren().add(e);
              }
          }
          if(menu.getParentId()==0L){
              menuTree.add(menu);
          }
        }
      return menuTree;
    }
}
