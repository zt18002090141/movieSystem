package com.system.service;

import com.system.entity.User;
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
public interface UserService extends IService<User> {
   // List<Long> getNavMenuIds(Long userId);
    public String getUserAuthorityInfo(Long userId);
//    清空缓存
    public void clearUserAuthorityInfo(String userName);

    public User getByUsername(String userName);

    public void clearUserAuthorityInfoMenuId(Long menuId);

    public void clearUserAuthorityInfoByRoleId(Long roleId);

}
