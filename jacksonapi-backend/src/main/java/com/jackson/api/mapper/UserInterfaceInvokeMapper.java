package com.jackson.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackson.apicommon.model.entity.UserInterfaceInvoke;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户调用接口 Mapper 接口
 *
 * @author jackson
 */
@Mapper
public interface UserInterfaceInvokeMapper extends BaseMapper<UserInterfaceInvoke> {

    /**
     * 获取最多调用次数的接口列表
     * @param limit
     * @return
     */
    List<UserInterfaceInvoke> listTopInvokeInterface(int limit);

}
