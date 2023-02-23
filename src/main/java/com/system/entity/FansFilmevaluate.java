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
 * 
 * </p>
 *
 * @author Byterain
 * @since 2022-12-11
 */
@Data
@TableName("sys_fans_filmevaluate")
public class FansFilmevaluate  {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableField("fid")
    private Long fid;

    /**
     * 电影di
     */
    @TableField("filmid")
    private Long filmid;


}
