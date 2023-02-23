package com.system.service;

import com.system.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Byterain
 * @since 2022-11-29
 */
public interface RoleService extends IService<Role> {

//    根据用户编号userid获得该用户的角色信息
  public List<Role> listRolesByUserId(Long userId);

}
