package com.jackson.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jackson.apicommon.model.entity.InterfaceInfo;

/**
 * 接口信息服务
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验接口
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
