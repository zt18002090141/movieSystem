package com.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.system.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
@TableName("sys_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    //编辑角色 : 返回的角色对象，需要包含该角色所能操作的菜单编号
    @TableField(exist = false)
    private List<Long> menuIds;


}
