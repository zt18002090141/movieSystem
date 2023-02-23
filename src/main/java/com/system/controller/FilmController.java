package com.system.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.common.Result;
import com.system.common.lang.Const;
import com.system.entity.Film;
import com.system.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.system.common.BaseController;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 电影表 前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2022-12-08
 */
@Slf4j
@RestController
@RequestMapping("/system/film")
public class FilmController extends BaseController {

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:film:list')")
    public Result getFilmList(String name){

      Page<Film> filePage=filmService.page(getPage(),new QueryWrapper<Film>().like(StrUtil.isNotBlank(name),"name",name));
       filePage.getRecords().forEach(f->{

         // f.setCategory(filmService.getCategoryById(f.getCategoryId()));
           //LIST

           f.setCategory(Arrays.asList(categoryService.getById(f.getCategoryId())));
//           System.out.println(filmService.getCategoryById(f.getCategoryId()).toString());
       });
        return Result.success(filePage);
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:film:list')")
    public  Result getListById(@PathVariable Long id){
        Film film=filmService.getById(id);
        return  Result.success(film);
    }


    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:film:save')")
    public Result save(@RequestBody Film film){
        film.setCreated(LocalDateTime.now());
//        film.setGrade(Const.IS_DELETE);
//        film.setCategoryId(Const.CATEGORY);
        System.out.println();
        filmService.save(film);
        return Result.success(film);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:film:update')")
    public Result update(@RequestBody Film film){
        film.setUpdated(LocalDateTime.now());
        filmService.updateById(film);
        return Result.success(film);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:film:delete')")
    public Result del(@RequestBody Long[] ids){
     //   filmService.updateBatchById(ids,)
        filmService.removeByIds(Arrays.asList(ids));
        return Result.success("");
    }


}
