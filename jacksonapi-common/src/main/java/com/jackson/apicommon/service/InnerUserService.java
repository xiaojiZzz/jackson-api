package com.jackson.apicommon.service;

import com.jackson.apicommon.model.entity.User;

/**
 * 内部用户服务
 */
public interface InnerUserService {

    /**
     * 从数据库中查找是否已分配给用户密钥（accessKey）
     *
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
