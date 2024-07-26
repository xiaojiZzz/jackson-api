package com.jackson.apicommon.service;

/**
 * 内部用户接口调用服务
 */
public interface InnerUserInterfaceInvokeService {

    /**
     * 调用接口统计
     *
     * @param interfaceId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceId, long userId);

    /**
     * 判断用户是否有剩余调用次数
     * @param interfaceId
     * @param userId
     * @return
     */
    boolean hasLeftNum(long interfaceId, long userId);
}
