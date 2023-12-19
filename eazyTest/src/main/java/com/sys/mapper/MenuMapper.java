package com.sys.mapper;

import com.sys.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author BlackWarm
 * @since 2023-09-27
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    // @Param("") 括号里填的是 xml 中语句的对应字段
    public List<Menu> getMenuListByUserId(@Param("userId") Integer userId, @Param("parentId") Integer parentId);

}
