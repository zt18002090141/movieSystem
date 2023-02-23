package com.system.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.common.Result;
import com.system.common.lang.Const;
import com.system.entity.Fans;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.system.common.BaseController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2022-12-08
 */
@RestController
@Slf4j
@RequestMapping("/system/fans")
public class FansController extends BaseController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //获得所有的粉丝信息
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:fans:list')")
    public Result list(String username){ //name是搜索栏中内容，有name就进行模糊查询
        //查询分页数据
        Page<Fans> fansPage = fansService.page(getPage(),new QueryWrapper<Fans>().like(StrUtil.isNotBlank(username),"username",username));
        return Result.success(fansPage);
    }

    //根据角色id获得粉丝信息
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:fans:list')")
    public Result info(@PathVariable Long id){
        Fans byId = fansService.getById(id);
        return Result.success(byId);
    }

    //编辑或添加粉丝
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:fans:update')")
    public Result update(@RequestBody Fans fans){
        //设置角色更新时间
        fans.setUpdated(LocalDateTime.now());
        fansService.updateById(fans);
    //    log.info("更新");
        return Result.success(fans);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:fans:save')")
    public Result save(@RequestBody Fans fans){
        fans.setCreated(LocalDateTime.now());
        fans.setAvatar(Const.DEFAULT_AVATAR);
        fans.setDeltag(Const.IS_DELETE);
        fans.setPassword(bCryptPasswordEncoder.encode(Const.DEFAULT_PWD));
        fansService.save(fans);
        //  log.info("添加");
        return Result.success(fans);

    }

    //删除分类
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:fans:delete')")
    public Result delete(@RequestBody Long[] fansIds){
        fansService.removeByIds(Arrays.asList(fansIds));
        return Result.success("删除成功");
    }

    //重置密码
    @PostMapping("/repass/{id}")
    public Result repass(@PathVariable Long id){
        Fans byId = fansService.getById(id);
        byId.setPassword(bCryptPasswordEncoder.encode(Const.DEFAULT_PWD));
        byId.setUpdated(LocalDateTime.now());
        fansService.updateById(byId);
        return Result.success("");
    }


}
