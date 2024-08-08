package com.jackson.apicommon.service;

import com.jackson.apicommon.model.entity.InterfaceInfo;

/**
 * 内部接口服务
 *
 * @author jackson
 */
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询模拟接口是否存在（请求路径，请求方法，请求参数）
     *
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);
}
