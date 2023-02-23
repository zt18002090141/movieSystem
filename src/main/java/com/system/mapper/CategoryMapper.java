package com.system.mapper;

import com.system.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Byterain
 * @since 2022-12-07
 */
public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> getCategoryById(Long id);
}
