package com.jackson.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackson.api.common.StatusCode;
import com.jackson.api.exception.BusinessException;
import com.jackson.api.service.UserInterfaceInvokeService;
import com.jackson.api.mapper.UserInterfaceInvokeMapper;
import com.jackson.apicommon.model.entity.UserInterfaceInvoke;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户调用接口服务实现类
 *
 * @author jackson
 */
@Service
public class UserInterfaceInvokeServiceImpl extends ServiceImpl<UserInterfaceInvokeMapper, UserInterfaceInvoke>
        implements UserInterfaceInvokeService {

    @Resource
    private UserInterfaceInvokeMapper userInterfaceInvokeMapper;

    @Override
    public void validUserInterfaceInvoke(UserInterfaceInvoke userInterfaceInvoke, boolean add) {
        if (userInterfaceInvoke == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        // 创建时，所有参数必须非空
        if (add) {
            if (userInterfaceInvoke.getInterfaceId() <= 0 || userInterfaceInvoke.getUserId() <= 0) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "接口或用户不存在");
            }
        }
        if (userInterfaceInvoke.getLeftNum() <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "剩余次数不能小于 0");
        }
    }

    @Override
    public boolean invokeCount(long interfaceId, long userId) {
        // 判断
        if (interfaceId <= 0 || userId <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        // todo 需要考虑事务
        UpdateWrapper<UserInterfaceInvoke> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceId", interfaceId);
        updateWrapper.eq("userId", userId);
        updateWrapper.gt("leftNum", 0);
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return this.update(updateWrapper);
    }

    @Override
    public boolean hasLeftNum(long interfaceId, long userId) {
        // 判断
        if (interfaceId <= 0 || userId <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInvoke> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceId", interfaceId);
        queryWrapper.eq("userId", userId);
        UserInterfaceInvoke userInterfaceInvoke = userInterfaceInvokeMapper.selectOne(queryWrapper);
        return userInterfaceInvoke.getLeftNum() > 0;
    }
}