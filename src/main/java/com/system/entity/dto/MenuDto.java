package com.system.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class MenuDto implements Serializable {
    private Long id;
    private String icon;
    private String title;
    private String path;
    private String name;
    private String component;
    List<MenuDto> children=new ArrayList<>();
}
