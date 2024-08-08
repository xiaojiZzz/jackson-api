package com.jackson.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jackson.api.annotation.AuthCheck;
import com.jackson.api.common.BaseResponse;
import com.jackson.api.common.DeleteRequest;
import com.jackson.api.common.ResultUtils;
import com.jackson.api.common.StatusCode;
import com.jackson.api.constant.CommonConstant;
import com.jackson.api.constant.UserConstant;
import com.jackson.api.exception.BusinessException;
import com.jackson.api.model.dto.userinterfaceinvoke.UserInterfaceInvokeAddRequest;
import com.jackson.api.model.dto.userinterfaceinvoke.UserInterfaceInvokeQueryRequest;
import com.jackson.api.model.dto.userinterfaceinvoke.UserInterfaceInvokeUpdateRequest;
import com.jackson.api.service.UserInterfaceInvokeService;
import com.jackson.api.service.UserService;
import com.jackson.apicommon.model.entity.User;
import com.jackson.apicommon.model.entity.UserInterfaceInvoke;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口调用管理
 *
 * @author jackson
 */
@RestController
@RequestMapping("userInterfaceInvoke")
public class UserInterfaceInvokeController {

    @Resource
    private UserInterfaceInvokeService userInterfaceInvokeService;

    @Resource
    private UserService userService;

    /**
     * 增加用户接口调用次数（仅管理员可使用）
     *
     * @param userInterfaceInvokeAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInvoke(@RequestBody UserInterfaceInvokeAddRequest userInterfaceInvokeAddRequest, HttpServletRequest request) {
        if (userInterfaceInvokeAddRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        UserInterfaceInvoke userInterfaceInvoke = new UserInterfaceInvoke();
        BeanUtils.copyProperties(userInterfaceInvokeAddRequest, userInterfaceInvoke);

        // 校验
        userInterfaceInvokeService.validUserInterfaceInvoke(userInterfaceInvoke, true);
        User loginUser = userService.getLoginUser(request);
        userInterfaceInvoke.setUserId(loginUser.getId());
        boolean result = userInterfaceInvokeService.save(userInterfaceInvoke);
        if (!result) {
            throw new BusinessException(StatusCode.OPERATION_ERROR);
        }

        // 调用 save() 方法后，已经将接口 id 赋值给了 userInterfaceInvoke 对象，不需要再从数据库读取
        long newUserInterfaceInvokeId = userInterfaceInvoke.getId();
        return ResultUtils.success(newUserInterfaceInvokeId);
    }

    /**
     * 删除（仅管理员可使用）
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInvoke(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();

        // 判断是否存在
        UserInterfaceInvoke oldUserInterfaceInvoke = userInterfaceInvokeService.getById(id);
        if (oldUserInterfaceInvoke == null || oldUserInterfaceInvoke.getUserId() == null) {
            throw new BusinessException(StatusCode.NOT_FOUND_ERROR);
        }

        // 仅本人或管理员可删除
        if (!oldUserInterfaceInvoke.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(StatusCode.NO_AUTH_ERROR);
        }
        boolean b = userInterfaceInvokeService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员可使用）
     *
     * @param userInterfaceInvokeUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInvoke(@RequestBody UserInterfaceInvokeUpdateRequest userInterfaceInvokeUpdateRequest,
                                                           HttpServletRequest request) {
        if (userInterfaceInvokeUpdateRequest == null || userInterfaceInvokeUpdateRequest.getId() <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        UserInterfaceInvoke userInterfaceInvoke = new UserInterfaceInvoke();
        BeanUtils.copyProperties(userInterfaceInvokeUpdateRequest, userInterfaceInvoke);

        // 参数校验
        userInterfaceInvokeService.validUserInterfaceInvoke(userInterfaceInvoke, false);
        User user = userService.getLoginUser(request);
        long id = userInterfaceInvokeUpdateRequest.getId();

        // 判断是否存在
        UserInterfaceInvoke oldUserInterfaceInvoke = userInterfaceInvokeService.getById(id);
        if (oldUserInterfaceInvoke == null || oldUserInterfaceInvoke.getUserId() == null) {
            throw new BusinessException(StatusCode.NOT_FOUND_ERROR);
        }

        // 仅本人或管理员可修改
        if (!oldUserInterfaceInvoke.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(StatusCode.NO_AUTH_ERROR);
        }
        boolean result = userInterfaceInvokeService.updateById(userInterfaceInvoke);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取（仅管理员可使用）
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInvoke> getUserInterfaceInvokeById(long id) {
        if (id <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        UserInterfaceInvoke userInterfaceInvoke = userInterfaceInvokeService.getById(id);
        return ResultUtils.success(userInterfaceInvoke);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param userInterfaceInvokeQueryRequest
     * @return
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<UserInterfaceInvoke>> listUserInterfaceInvoke(UserInterfaceInvokeQueryRequest userInterfaceInvokeQueryRequest) {
        if (userInterfaceInvokeQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        UserInterfaceInvoke userInterfaceInvokeQuery = new UserInterfaceInvoke();
        BeanUtils.copyProperties(userInterfaceInvokeQueryRequest, userInterfaceInvokeQuery);
        QueryWrapper<UserInterfaceInvoke> queryWrapper = new QueryWrapper<>(userInterfaceInvokeQuery);
        List<UserInterfaceInvoke> userInterfaceInvokeList = userInterfaceInvokeService.list(queryWrapper);
        return ResultUtils.success(userInterfaceInvokeList);
    }

    /**
     * 分页获取列表（仅管理员可使用）
     *
     * @param userInterfaceInvokeQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInvoke>> listUserInterfaceInvokeByPage(UserInterfaceInvokeQueryRequest userInterfaceInvokeQueryRequest,
                                                                                 HttpServletRequest request) {
        if (userInterfaceInvokeQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        UserInterfaceInvoke userInterfaceInvokeQuery = new UserInterfaceInvoke();
        BeanUtils.copyProperties(userInterfaceInvokeQueryRequest, userInterfaceInvokeQuery);
        long current = userInterfaceInvokeQueryRequest.getCurrent();
        long size = userInterfaceInvokeQueryRequest.getPageSize();
        String sortField = userInterfaceInvokeQueryRequest.getSortField();
        String sortOrder = userInterfaceInvokeQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInvoke> queryWrapper = new QueryWrapper<>(userInterfaceInvokeQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInvoke> userInterfaceInvokePage = userInterfaceInvokeService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(userInterfaceInvokePage);
    }
}
