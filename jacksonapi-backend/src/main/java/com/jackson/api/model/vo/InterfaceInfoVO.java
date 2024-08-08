package com.jackson.api.model.vo;

import com.jackson.apicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 接口信息封装视图
 *
 * @author jackson
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class InterfaceInfoVO extends InterfaceInfo {

    /**
     * 调用次数
     */
    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}
