package com.system.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import com.system.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author Byterain
 * @since 2022-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_orders")
public class Orders extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 排片id
     */
    @TableField("aid")
    private Long aid;

    /**
     * 座位号
     */
    @TableField("seats")
    private String seats;

    /**
     * 金额
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 支付时间
     */
    @TableField("pay_at")
    private LocalDateTime payAt;

    @TableField("is_refund")
    private Integer isRefund;
    /**
     * 是否删除
     */
    @TableField("is_delete")
    @TableLogic
    private Integer isDelete;


}
