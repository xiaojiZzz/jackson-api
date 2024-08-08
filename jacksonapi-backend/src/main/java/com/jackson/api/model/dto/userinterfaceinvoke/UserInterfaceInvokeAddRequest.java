package com.jackson.api.model.dto.userinterfaceinvoke;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建用户调用接口请求
 *
 * @author jackson
 */
@Data
public class UserInterfaceInvokeAddRequest implements Serializable {

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;
}
