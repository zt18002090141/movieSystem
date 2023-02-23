package com.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDate;
import java.util.List;

import com.system.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 电影表
 * </p>
 *
 * @author Byterain
 * @since 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_film")
public class Film extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("name")
    private String name;

    @TableField("release_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseTime;

    @TableField("category_id")
    private Long categoryId;

    @TableField("region")
    private String region;

    @TableField("cover")
    private String cover;

    @TableField("duration")
    private Integer duration;

    @TableField("is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField("grade")
    private float grade;

    @TableField(exist = false)
    private List<Category> category;


}
