package com.system.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.common.Result;
import com.system.entity.*;

import com.system.entity.dto.FilmEvaluateDto;
import com.system.service.FansService;
import org.jcp.xml.dsig.internal.dom.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.system.common.BaseController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 电影评论表 前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2022-12-09
 */
@RestController
@RequestMapping("/system/filmEvaluate")
public class FilmEvaluateController extends BaseController {

    private String olduname;
    private String oldfname;

    @GetMapping("/list")
    //查询所有权限
    public Result list(Long id){
        //先查询评论表数据 在通过评论表里面数据来获取username 和 filmname
        Page<FilmEvaluate> page = new Page<>();
        if (id == -1 || id == null){
            page = filmEvaluateService.page(getPage(),new QueryWrapper<FilmEvaluate>().orderByDesc("created"));
        }else
        {
            page  = filmEvaluateService.page(getPage(),new QueryWrapper<FilmEvaluate>().like(StrUtil.isNotBlank(id.toString()),"id",id).orderByDesc("created"));
        }
        Page<FilmEvaluateDto> page2 = new Page<>();
        //拷贝除了records 的数据
        BeanUtils.copyProperties(page,page2,"records");
        List<FilmEvaluateDto> list=  page.getRecords().stream().map(r ->{
            FilmEvaluateDto dto = new FilmEvaluateDto();
            //获取用户name
            Fans uid = fansService.getById(r.getUid());
            //获取电影name
            Film fid = filmService.getById(r.getFid());
            BeanUtils.copyProperties(r,dto);
            //设置电影名字 和 用户名字
            dto.setUname(uid.getUsername());
            dto.setFname(fid.getName());

            return dto;
        }).collect(Collectors.toList());
        page2.setRecords(list);
        return Result.success(page2);
    }

    @GetMapping("/info/{id}")
    //编辑权限
    public Result info(@PathVariable Long id){
        FilmEvaluate filmEvaluate = filmEvaluateService.getById(id);
        olduname = fansService.getById(filmEvaluate.getUid()).getUsername();
        oldfname = filmService.getById(filmEvaluate.getFid()).getName();
        FilmEvaluateDto dto = new FilmEvaluateDto();
        BeanUtils.copyProperties(filmEvaluate,dto);
        dto.setUname(olduname);
        dto.setFname(oldfname);
        return Result.success(dto);
    }

    @PostMapping("/delete")
    //删除权限
    public Result info(@RequestBody Long[] ids){
        //删除关联
        List<Long> aids = Arrays.asList(ids);

        aids.forEach(i ->{
            //查询影评
            FilmEvaluate filmEvaluate = filmEvaluateService.getById(i);
            fansFilmevaluateService.remove(new QueryWrapper<FansFilmevaluate>().eq("fid",filmEvaluate.getUid()).eq("filmid",filmEvaluate.getFid()));
        });

        filmEvaluateService.removeByIds(aids);



        return Result.success("");
    }

    @PostMapping("/save")
    @Transactional
    //新建权限
    public Result save(@RequestBody  FilmEvaluateDto filmEvaluateDto){
        String fname = filmEvaluateDto.getFname();
        String uname = filmEvaluateDto.getUname();

        if (filmService.count(new QueryWrapper<Film>().eq("name",fname)) == 0){
            return Result.fail("添加失败,请输入正确的电影名称");
        }
        else if (fansService.count(new QueryWrapper<Fans>().eq("username",uname)) == 0){
            return Result.fail("添加失败,请输入正确的用户名称名称");
        }
//        (fansService.count(new QueryWrapper<FansFilmevaluate>().eq("uid",filmEvaluate.getUid()).eq("fmid",filmEvaluate.getFid())) > 0)
        Long fid = filmService.getOne(new QueryWrapper<Film>().eq("name",fname)).getId();
        Long uid = fansService.getOne(new QueryWrapper<Fans>().eq("username",uname)).getId();
        if (filmEvaluateService.count(new QueryWrapper<FilmEvaluate>().eq("fid",fid).eq("uid",uid)) > 0){
            return Result.fail("添加失败，对相同电影只能评价一次");
        }

        FilmEvaluate filmEvaluate = new FilmEvaluate();
        //转换类型
        BeanUtils.copyProperties(filmEvaluateDto,filmEvaluate);
        //获取id
        filmEvaluate.setFid(filmService.getOne(new QueryWrapper<Film>().eq("name",fname)).getId());
        filmEvaluate.setUid(fansService.getOne(new QueryWrapper<Fans>().eq("username",uname)).getId());

        //设置 创建时间 和 修改时间 和状态
        filmEvaluate.setCreated(LocalDateTime.now());
        filmEvaluate.setUpdated(LocalDateTime.now());
        filmEvaluate.setStatu(1);
        //在评论表里面插入
        filmEvaluateService.save(filmEvaluate);
        //在用户与电影关联表中加入;
        FansFilmevaluate f_f = new FansFilmevaluate();
        f_f.setFid(filmEvaluate.getUid());
        f_f.setFilmid(filmEvaluate.getFid());
        fansFilmevaluateService.save(f_f);
        return Result.success("添加成功");
    }

    @PostMapping("/update")
    //修改权限
    @Transactional
    public Result update(@RequestBody FilmEvaluateDto filmEvaluateDto){
        //获取对象
        String fname = filmEvaluateDto.getFname();
        String uname = filmEvaluateDto.getUname();
        if (filmService.count(new QueryWrapper<Film>().eq("name",fname)) == 0){
            return Result.fail("修改失败,请输入正确的电影名称");
        }
        else if (fansService.count(new QueryWrapper<Fans>().eq("username",uname)) == 0){
            return Result.fail("修改失败,请输入正确的用户名称名称");
        }
        //判断是否重复评论 判断olduname 和 oldfname 新的比较， 来判断用户id是否存在
        if (!fname.equals(oldfname) || !uname.equals(oldfname)){ //如果存在 老的和新的有一个不一样 那么需要进行判断
            //通过新的name 获取记录，判断用户是否对该电影进行过评价
            //分别获取用户id 或者 电影id
            Long uid = fansService.getOne(new QueryWrapper<Fans>().eq("username",uname)).getId();
            Long fid = filmService.getOne(new QueryWrapper<Film>().eq("name",fname)).getId();
            //判断评论表里面是否有一模一样的记录
            if (filmEvaluateService.count(new QueryWrapper<FilmEvaluate>().eq("uid",uid).eq("fid",fid) ) > 0){
                String s = "修改失败，" + uname + "用户对《" + fname + "》已经有过评价";
                return Result.fail(s);
            }
        }


        FilmEvaluate filmEvaluate = new FilmEvaluate();
        //转换类型
        BeanUtils.copyProperties(filmEvaluateDto,filmEvaluate);
        //获取id
        filmEvaluate.setFid(filmService.getOne(new QueryWrapper<Film>().eq("name",fname)).getId());
        filmEvaluate.setUid(fansService.getOne(new QueryWrapper<Fans>().eq("username",uname)).getId());

        //时间和 状态
        filmEvaluate.setUpdated(LocalDateTime.now());
        filmEvaluate.setStatu(filmEvaluate.getStatu());

        filmEvaluateService.updateById(filmEvaluate);

        //先删除再加
        fansFilmevaluateService.remove(new QueryWrapper<FansFilmevaluate>().eq("fid",filmEvaluate.getUid()).eq("filmid",filmEvaluate.getFid()));

        FansFilmevaluate f_f = new FansFilmevaluate();
        f_f.setFid(filmEvaluate.getUid());
        f_f.setFilmid(filmEvaluate.getFid());
        fansFilmevaluateService.save(f_f);

        return Result.success("修改成功");
    }

}
