package com.jackson.api.model.dto.userinterfaceinvoke;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新用户调用接口请求
 *
 * @author jackson
 */
@Data
public class UserInterfaceInvokeUpdateRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
