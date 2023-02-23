package com.system.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.common.Result;
import com.system.common.lang.Const;
import com.system.entity.Category;
import com.system.entity.User;
import com.system.util.ImageUploadUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.system.common.BaseController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static cn.hutool.core.io.FileUtil.copyFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2022-12-07
 */
@RestController
@RequestMapping("/system/category")
public class CategoryController extends BaseController {

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:category:list')")
    public Result getCategoryList(String name){
        Page<Category> categoryPage=categoryService.page(getPage(),new QueryWrapper<Category>().like(StrUtil.isNotBlank(name),"name",name));
        return Result.success(categoryPage);
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:category:list')")
    public Result getCategoryInfoById(@PathVariable Long id){
        Category category = categoryService.getById(id);
        return Result.success(category);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:category:save')")
    public Result save(@RequestBody Category category){
      Category category1=  categoryService.getOne(new QueryWrapper<Category>().eq("name",category.getName()));
      if (category1 == null){
          category.setCreated(LocalDateTime.now());
          category.setStatu(Const.STATUS_ON);
          categoryService.save(category);
          return Result.success(category);
      }else {
          return Result.fail("该分类已存在，添加失败！");
      }

    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:category:update')")
    public Result update(@RequestBody Category category){
        category.setUpdated(LocalDateTime.now());
        categoryService.updateById(category);
        return Result.success(category);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:category:delete')")
    public Result delete(@RequestBody Long[] ids){
        categoryService.removeByIds(Arrays.asList(ids));
        return Result.success("");
    }


}
