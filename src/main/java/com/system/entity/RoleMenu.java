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
@TableName("sys_role_menu")
public class RoleMenu  {

    private static final long serialVersionUID = 1L;

    @TableField("role_id")
    private Long roleId;

    @TableField("menu_id")
    private Long menuId;


}
