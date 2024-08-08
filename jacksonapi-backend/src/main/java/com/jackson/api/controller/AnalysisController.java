package com.jackson.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jackson.api.annotation.AuthCheck;
import com.jackson.api.common.BaseResponse;
import com.jackson.api.common.ResultUtils;
import com.jackson.api.common.StatusCode;
import com.jackson.api.constant.UserConstant;
import com.jackson.api.exception.BusinessException;
import com.jackson.api.mapper.UserInterfaceInvokeMapper;
import com.jackson.api.model.vo.InterfaceInfoVO;
import com.jackson.api.service.InterfaceInfoService;
import com.jackson.apicommon.model.entity.InterfaceInfo;
import com.jackson.apicommon.model.entity.UserInterfaceInvoke;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分析控制器
 *
 * @author jackson
 */
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private UserInterfaceInvokeMapper userInterfaceInvokeMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 获取最多调用次数的接口列表
     *
     * @return
     */
    @GetMapping("/top/interface/invoke")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterface() {
        List<UserInterfaceInvoke> userInterfaceInvokeList = userInterfaceInvokeMapper.listTopInvokeInterface(3);
        Map<Long, List<UserInterfaceInvoke>> interfaceInfoIdObjMap = userInterfaceInvokeList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInvoke::getInterfaceId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOList);
    }
}
