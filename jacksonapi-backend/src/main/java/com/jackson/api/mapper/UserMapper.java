package com.jackson.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackson.apicommon.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 *
 * @author jackson
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
