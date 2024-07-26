package com.jackson.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackson.apicommon.model.entity.InterfaceInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {
}