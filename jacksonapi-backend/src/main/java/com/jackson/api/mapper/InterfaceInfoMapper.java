package com.jackson.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackson.apicommon.model.entity.InterfaceInfo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 接口 Mapper 接口
 *
 * @author jackson
 */
@Mapper
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {
}