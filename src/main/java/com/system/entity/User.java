package com.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.List;

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
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("avatar")
    private String avatar;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("city")
    private String city;

    @TableField("last_login")
    private LocalDateTime lastLogin;

    @TableField(exist = false)
    private List<Role> roles;

}
