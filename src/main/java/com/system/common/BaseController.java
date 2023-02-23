package com.system.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.service.*;
import com.system.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;


public class BaseController {

    @Autowired
    public HttpServletRequest request;

    @Autowired
    public RedisUtil redisUtil;

    @Autowired
    public MenuService menuService;

    @Autowired
    public RoleMenuService roleMenuService;

    @Autowired
    public RoleService roleService;

    @Autowired
    public UserService userService;

   @Autowired
   public UserRoleService userRoleService;

   @Autowired
   public CategoryService categoryService;

   @Autowired
   public FilmService filmService;

   @Autowired
   public PosterService posterService;

   @Autowired
   public FansService fansService;

   @Autowired
   public  ArrangementService arrangementService;


   @Autowired
   public OrdersService ordersService;

   @Autowired
   public FilmEvaluateService filmEvaluateService;

   @Autowired
   public FansFilmevaluateService fansFilmevaluateService;

//    获得分页数据
    public Page getPage(){
        int current= ServletRequestUtils.getIntParameter(request,"current",1);
        int size = ServletRequestUtils.getIntParameter(request,"size",5);
        return new Page(current,size);
    }
}
