package com.jackson.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jackson.apicommon.model.entity.UserInterfaceInvoke;

/**
 * 用户调用接口服务
 *
 * @author jackson
 */
public interface UserInterfaceInvokeService extends IService<UserInterfaceInvoke> {

    /**
     * 校验用户接口调用
     *
     * @param userInterfaceInvoke
     * @param add
     */
    void validUserInterfaceInvoke(UserInterfaceInvoke userInterfaceInvoke, boolean add);

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
     *
     * @param interfaceId
     * @param userId
     * @return
     */
    boolean hasLeftNum(long interfaceId, long userId);

}
