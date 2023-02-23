package com.system.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.common.Result;
import com.system.entity.Arrangement;
import com.system.entity.Fans;
import com.system.entity.Orders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.system.common.BaseController;

import java.util.Arrays;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2022-12-09
 */
@RestController
@RequestMapping("/system/order")
public class OrdersController extends BaseController {

    @GetMapping("/list")
//    todo:权限
    public Result getOrderList(String id){
        Page<Orders> orderPage= ordersService.page(getPage(), new QueryWrapper<Orders>().like(StrUtil.isNotBlank(id), "id", id));
        return Result.success(orderPage);
    }

//    private Object toString(Long id) {
//    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:order:delete')")
    public Result del(@RequestBody Long[] ids){
        //   filmService.updateBatchById(ids,)
        ordersService.removeByIds(Arrays.asList(ids));
        return Result.success("");
    }

    @GetMapping("/info/{uid}")
    public Result getFansInfoByUid(@PathVariable Long uid){
      Fans fans=  fansService.getOne(new QueryWrapper<Fans>().eq("id",uid));
        return Result.success(fans);
    }

    @GetMapping("/arrinfo/{aid}")
    public Result getArrangement(@PathVariable Long aid){
     Arrangement arrangement= arrangementService.getOne(new QueryWrapper<Arrangement>().eq("id",aid));
     return Result.success(arrangement);
    }

}
