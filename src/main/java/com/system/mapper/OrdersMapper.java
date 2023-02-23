package com.system.mapper;

import com.system.entity.Fans;
import com.system.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author Byterain
 * @since 2022-12-09
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    Fans getFansInfoByUid(Long uid);
}
