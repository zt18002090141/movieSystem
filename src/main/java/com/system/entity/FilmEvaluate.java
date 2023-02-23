package com.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.system.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 电影评论表
 * </p>
 *
 * @author Byterain
 * @since 2022-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_film_evaluate")
public class FilmEvaluate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 电影id
     */
    @TableField("fid")
    private Long fid;

    /**
     * 用户id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 评分
     */
    @TableField("star")
    private Integer star;

    /**
     * 评论内容
     */
    @TableField("comment")
    private String comment;


}
