package com.sys.mapper;

import com.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author BlackWarm
 * @since 2023-09-17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<String> getRoleNameByUserId(Integer userId);

}
