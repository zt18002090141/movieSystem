package com.system.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.common.Result;
import com.system.common.lang.Const;
import com.system.entity.Arrangement;
import com.system.entity.Category;
import com.system.entity.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.system.common.BaseController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 排片表 前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2022-12-08
 */
@Slf4j
@RestController
@RequestMapping("/system/arrangement")
public class ArrangementController extends BaseController {


    //获取排片列表
    @GetMapping("/list")
    //TODO:模糊查询时间段查询
    @PreAuthorize("hasAuthority('sys:arrangement:list')")
    public Result list(String name,String time){
        log.info("-------------------------------");
        log.info(time);
        Page<Arrangement> arrangementPage = arrangementService.page(getPage(),new QueryWrapper<Arrangement>().orderByDesc("end_time").like(StrUtil.isNotBlank(name),"name",name));



        //动态设置电影的排片状态
        //1 正在播放  2 已经失效 3 即将开始
        //先判断 大的日期  在判断小的日期
        arrangementPage.getRecords().forEach(a ->{
            //上映日期的判断
            if (a.getDate().getTime() - new Date().getTime() <= 0){
                if (new Date().getTime() - a.getDate().getTime() >= a.getStartTime().getTime() && new Date().getTime() - a.getDate().getTime() <= a.getEndTime().getTime()){
                    a.setStatu(2);
                }else {
                    if (new Date().getTime() - a.getDate().getTime() < a.getStartTime().getTime() ){
                        a.setStatu(3);
                    }else if ((new Date().getTime() - a.getDate().getTime() > a.getEndTime().getTime())){
                        a.setStatu(1);
                    }
                }
            }else {
                a.setStatu(3);
            }
        });


        return Result.success(arrangementPage);
    }
    //删除排片
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:arrangement:delete')")
    public Result delete(@RequestBody Long[] ids){
        arrangementService.removeByIds(Arrays.asList(ids));
        return Result.success("");
    }
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:arrangement:list')")
    public Result info(@PathVariable Long id){
        Arrangement arrangement = arrangementService.getById(id);
        return Result.success(arrangement);
    }
    //tudo:  通过电影时长自动加时间
    @PostMapping ("/save")
    @PreAuthorize("hasAuthority('sys:arrangement:save')")
    public Result save(@RequestBody Arrangement arrangement){
        //设置时间
        arrangement.setCreated(LocalDateTime.now());
        arrangement.setUpdated(LocalDateTime.now());
        arrangement.setBoxOffice(0);

        //通过电影时长自动设置结束时间 1. 获取电影  2. 获取电影时长  处理数据
        //1.获取时长  毫秒数
        Long timeLength = filmService.getOne(new QueryWrapper<Film>().eq("name", arrangement.getName())).getDuration() * 60 * 1000L + arrangement.getStartTime().getTime();

        arrangement.setEndTime(new Date(timeLength));

        //判断放映时间是不是早于上映时间 1. 获取电影上映日期
        // LocalDate name1 = filmService.getOne(new QueryWrapper<Film>().eq("name", arrangement.getName())).getReleaseTime();



        //设置电影id 状态
        setfId(arrangement);
        arrangement.setStatu(Const.STATUS_ON);
        //设置电影类型
        Film name = filmService.getOne(new QueryWrapper<Film>().eq("name", arrangement.getName()));
        Long category = name.getCategoryId();

        // arrangement.setCategory();
//        arrangement.setCategory_id(category);
        arrangementService.save(arrangement);

        return Result.success(arrangement);
    }
    @PostMapping ("/update")
//    @PreAuthorize("hasAuthority('sys:arrangement:update')")
    public Result info(@RequestBody Arrangement arrangement){
        arrangement.setUpdated(LocalDateTime.now());

        setfId(arrangement);
        //通过电影时长自动设置结束时间 1. 获取电影  2. 获取电影时长  处理数据
        //1.获取时长  毫秒数
        Long timeLength = filmService.getOne(new QueryWrapper<Film>().eq("name", arrangement.getName())).getDuration() * 60 * 1000L + arrangement.getStartTime().getTime();
        arrangement.setEndTime(new Date(timeLength));

        arrangementService.updateById(arrangement);
        return Result.success(arrangement);
    }

    public void setfId(Arrangement arrangement){
        String fname = arrangement.getName();
        Film film = filmService.getOne(new QueryWrapper<Film>().eq("name", fname));
        arrangement.setFid(film.getId());
    }
}
