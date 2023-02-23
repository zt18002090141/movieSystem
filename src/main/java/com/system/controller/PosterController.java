package com.system.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.common.Result;
import com.system.common.lang.Const;
import com.system.entity.Poster;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.system.common.BaseController;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * <p>
 * 轮播图 前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2022-12-08
 */
@RestController
@RequestMapping("/system/poster")
public class PosterController extends BaseController {

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:poster:list')")
    public Result getPosterList(String title){
        Page<Poster> posterPage=posterService.page(getPage(),new QueryWrapper<Poster>().like(StrUtil.isNotBlank(title),"title",title));
        return Result.success(posterPage);
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:poster:list')")
    public Result getPosterInfoById(@PathVariable Long id){
        Poster poster=posterService.getById(id);
        return Result.success(poster);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:poster:save')")
    public Result save(@RequestBody Poster poster){
        poster.setCreated(LocalDateTime.now());
        poster.setStatu(Const.STATUS_ON);
        posterService.save(poster);
        return Result.success(poster);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:poster:update')")
    public Result update(@RequestBody Poster poster){
        poster.setUpdated(LocalDateTime.now());
        posterService.updateById(poster);
        return Result.success(poster);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:poster:delete')")
    public Result delete(@RequestBody Long[] ids){
        posterService.removeByIds(Arrays.asList(ids));
        return Result.success("");
    }
}
