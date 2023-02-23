package com.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.system.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 排片表
 * </p>
 *
 * @author Byterain
 * @since 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_arrangement")

public class Arrangement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("fid")
    private Long fid;

    @TableField("name")
    private String name;

    @TableField("seat_number")
    private Integer seatNumber;

    @TableField("box_office")
    private Integer boxOffice;

    @TableField("price")
    private Double price;

    @TableField("type")
    private String type;

//    @TableField("date")
//    private LocalDate date;
//
//    @TableField("start_time")
//    private LocalTime startTime;
//
//    @TableField("end_time")
//    private LocalTime endTime;

    @TableField("date")
    //@DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date date;

    @TableField("start_time")
    //@DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date startTime;

    @TableField("end_time")
    // @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date endTime;

    @TableField(exist = false)
    private String category;

//    @TableField("category_id")
//    private Long category_id;
}
