package com.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
//@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_role")
public class UserRole  {

    private static final long serialVersionUID = 1L;

    @TableField("user_id")
    private Long userId;

    @TableField("role_id")
    private Long roleId;


}
