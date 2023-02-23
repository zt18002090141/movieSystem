package com.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.system.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Byterain
 * @since 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_fans")
public class Fans extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("avatar")
    private String avatar;

    @TableField("sex")
    private Integer sex;

    @TableField("email")
    private String email;

    @TableField("delTag")
    @TableLogic
    private Integer deltag;

    @TableField("wallet")
    private BigDecimal wallet;


}
