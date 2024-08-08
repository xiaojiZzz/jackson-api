package com.jackson.api.service.impl.inner;

import com.jackson.api.service.UserInterfaceInvokeService;
import com.jackson.apicommon.service.InnerUserInterfaceInvokeService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部用户接口调用服务实现类
 *
 * @author jackson
 */
@DubboService
public class InnerUserInterfaceInvokeServiceImpl implements InnerUserInterfaceInvokeService {

    @Resource
    private UserInterfaceInvokeService userInterfaceInvokeService;

    @Override
    public boolean invokeCount(long interfaceId, long userId) {
        return userInterfaceInvokeService.invokeCount(interfaceId, userId);
    }

    @Override
    public boolean hasLeftNum(long interfaceId, long userId) {
        return userInterfaceInvokeService.hasLeftNum(interfaceId, userId);
    }
}
